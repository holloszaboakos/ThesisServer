package hu.bme.thesis.logic.specimen

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

data class DOnePartRepresentation(
    override val objectiveCount: Int,
    private val permutation: IntArray,
    override var inUse: Boolean = true,
    override var costCalculated: Boolean = false,
    override var cost: Double = -1.0,
    override var iteration: Int = 0,
    override var orderInPopulation: Int = 0
) : ISpecimenRepresentation {

    private val lock = ReentrantReadWriteLock()

    constructor(data: Array<IntArray>) : this(
        data.sumOf { it.size },
        data.let {
            val objectiveCount = data.sumOf { it.size }
            val salesmanCount = data.size
            val permutation = IntArray(objectiveCount + salesmanCount - 1) { -1 }
            var counter = 0
            data.forEachIndexed { index, array ->
                array.forEach {
                    permutation[counter] = it
                    counter++
                }
                if (counter < permutation.size) {
                    permutation[counter] = objectiveCount + index
                    counter++
                }
            }
            permutation
        }
    )

    constructor(other: DOnePartRepresentation) : this(
        other.objectiveCount,
        other.permutation,
        other.inUse,
        other.costCalculated,
        other.cost,
        other.iteration
    )

    override val salesmanCount: Int = permutation.size - objectiveCount + 1
    override val permutationSize: Int = permutation.size

    override operator fun get(index: Int) = lock.read {
        permutation[index]
    }

    override operator fun set(index: Int, value: Int) = lock.write {
        permutation[index] = value
    }

    override fun indexOf(value: Int): Int = permutation.indexOf(value)

    override fun contains(value: Int): Boolean = permutation.contains(value)

    override fun <T> map(mapper: (Int) -> T): Flow<T> = permutation.map(mapper).asFlow()

    override fun forEach(operation: (Int) -> Unit) =
        lock.read { permutation.forEach(operation) }

    override fun forEachIndexed(operation: (Int, Int) -> Unit) =
        lock.read { permutation.forEachIndexed(operation) }

    override fun setEach(operation: (Int, Int) -> Int) = lock.write {
        permutation.forEachIndexed { index: Int, value: Int -> permutation[index] = operation(index, value) }
    }

    override fun <T> mapSlice(mapper: (slice: Flow<Int>) -> T): Flow<T> = lock.read {
        val result = mutableListOf<MutableList<Int>>(mutableListOf())
        permutation.forEach { value->
            if(value<objectiveCount)
                result.last().add(value)
            else
                result.add(mutableListOf())
        }
        result.asFlow().map { mapper(it.asFlow()) }
    }

    override fun forEachSlice(operation: (slice: Flow<Int>) -> Unit) = lock.read {
        runBlocking { mapSlice { it }.toList() }
            .forEach { slice -> operation(slice) }
    }

    override fun forEachSliceIndexed(operation: (index: Int, slice: Flow<Int>) -> Unit): Unit = lock.read {
        val collected = runBlocking { mapSlice { it }.toList() }
        collected.forEachIndexed { index, flow ->
            operation(index, flow)
        }
    }

    override fun slice(indices: IntRange): Flow<Int> = lock.read {
        flow {
            emitAll(permutation.slice(indices).asFlow())
        }
    }

    override fun shuffle() = lock.write { permutation.shuffle() }
    override fun first(selector: (Int) -> Boolean): Int = lock.read { permutation.first(selector) }

    override suspend fun setData(data: Flow<Flow<Int>>) {
        var counter = 0
        data.collectIndexed { index, array ->
            array.collect {
                permutation[counter] = it
                counter++
            }
            if (counter < permutation.size) {
                permutation[counter] = objectiveCount + index
                counter++
            }
        }
    }

    override fun getData(): Flow<Flow<Int>> {
        lock.read {
            return mapSlice { list -> list }
        }
    }

    override fun checkFormat(): Boolean {
        val contains = BooleanArray(permutationSize) { false }
        var result = true
        permutation.forEach {
            if (it !in permutation.indices || contains[it])
                result = false
            else
                contains[it] = true
        }
        return if (permutation.filter { it >= objectiveCount }.size != salesmanCount - 1)
            false
        else result
    }
}

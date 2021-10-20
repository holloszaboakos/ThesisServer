package hu.bme.thesis.logic.specimen

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

data class DOnePartRepresentation(
    override val objectiveCount: Int,
    private val permutation: IntArray,
    override var inUse: Boolean = true,
    override var costCalculated: Boolean = true,
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

    override fun <T> map(mapper: (Int) -> T): Sequence<T> = permutation.map(mapper).asSequence()

    override fun forEach(operation: (Int) -> Unit) =
        lock.read { permutation.forEach(operation) }

    override fun forEachIndexed(operation: (Int, Int) -> Unit) =
        lock.read { permutation.forEachIndexed(operation) }

    override fun setEach(operation: (Int, Int) -> Int) = lock.write {
        permutation.forEachIndexed { index: Int, value: Int -> permutation[index] = operation(index, value) }
    }

    override fun <T> mapSlice(mapper: (slice: Sequence<Int>) -> T): Sequence<T> = lock.read {
        sequence {
            var count = 0
            while (count < permutation.size) {
                yield(sequence {
                    permutation.slice(count until permutation.size).forEach {
                        count++
                        if (it < objectiveCount)
                            yield(it)
                        else
                            return@forEach
                    }
                })
            }
        }.map { mapper(it) }
    }

    override fun forEachSlice(operation: (slice: Sequence<Int>) -> Unit) = lock.read {
        mapSlice { it }.forEach { operation(it) }
    }

    override fun forEachSliceIndexed(operation: (index: Int, slice: Sequence<Int>) -> Unit) = lock.read {
        mapSlice { it }.forEachIndexed { index, sequence -> operation(index, sequence) }
    }

    override fun slice(indices: IntRange): Sequence<Int> = lock.read {
        sequence {
            yieldAll(permutation.slice(indices))
        }
    }

    override fun shuffle() = lock.write { permutation.shuffle() }
    override fun first(selector: (Int) -> Boolean): Int = lock.read { permutation.first(selector) }

    override fun setData(data: Sequence<Sequence<Int>>) {
        data.let {
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
        }
    }

    override fun getData(): Array<IntArray> {
        lock.read {
            return mapSlice { list -> list.toList().toIntArray() }.toList().toTypedArray()
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

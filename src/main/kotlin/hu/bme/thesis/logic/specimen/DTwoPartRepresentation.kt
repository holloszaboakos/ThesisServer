package hu.bme.thesis.logic.specimen

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

data class DTwoPartRepresentation(
    val permutation: IntArray,
    val sliceLengths: IntArray,
    override var inUse: Boolean = true,
    override var costCalculated: Boolean = false,
    override var cost: Double = -1.0,
    override var iteration: Int = 0,
    override var orderInPopulation: Int = -1,
) : ISpecimenRepresentation {

    constructor(data: Array<IntArray>) : this(
        data.let {
            val sum = mutableListOf<Int>()
            data.forEach { sum.addAll(it.toList()) }
            sum.toIntArray()
        },
        data.map { it.size }.toIntArray(),
    )

    constructor(other: DTwoPartRepresentation) : this(
        other.permutation,
        other.sliceLengths,
        other.inUse,
        other.costCalculated,
        other.cost,
        other.iteration
    )

    override val objectiveCount: Int = permutation.size
    override val salesmanCount: Int = sliceLengths.sum()
    override val permutationSize: Int = permutation.size

    override operator fun get(index: Int) = permutation[index]
    override operator fun set(index: Int, value: Int) {
        permutation[index] = value
    }

    override fun indexOf(value: Int): Int = permutation.indexOf(value)

    override fun contains(value: Int): Boolean = permutation.contains(value)

    override fun <T> map(mapper: (Int) -> T): Flow<T> = permutation.map(mapper).asFlow()

    override fun forEach(operation: (Int) -> Unit) = permutation.forEach(operation)
    override fun forEachIndexed(operation: (Int, Int) -> Unit) = permutation.forEachIndexed(operation)

    override fun setEach(operation: (Int, Int) -> Int) {
        permutation.forEachIndexed { index: Int, value: Int -> permutation[index] = operation(index, value) }
    }

    override fun <T> mapSlice(mapper: (slice: Flow<Int>) -> T): Flow<T> {
        var geneIndex = 0
        return flow {
            for (sliceLength in sliceLengths) {
                val slice = permutation.slice(geneIndex until (geneIndex + sliceLength))
                geneIndex += sliceLength
                emit(mapper(slice.asFlow()))
            }
        }
    }

    override fun forEachSlice(operation: (slice: Flow<Int>) -> Unit) {
        var geneIndex = 0
        sliceLengths.forEach { sliceLength ->
            val slice = permutation.slice(geneIndex until (geneIndex + sliceLength))
            operation(slice.asFlow())
            geneIndex += sliceLength
        }
    }

    override fun forEachSliceIndexed(operation: (index: Int, slice: Flow<Int>) -> Unit) {
        var geneIndex = 0
        sliceLengths.forEachIndexed { index, sliceLength ->
            val slice = permutation.slice(geneIndex until (geneIndex + sliceLength))
            operation(index, slice.asFlow())
            geneIndex += sliceLength
        }
    }

    override fun slice(indices: IntRange): Flow<Int> =
        permutation.slice(indices).asFlow()


    override fun shuffle() {
        permutation.shuffle()
    }

    override fun first(selector: (Int) -> Boolean): Int = permutation.first(selector)

    override suspend fun setData(data: Flow<Flow<Int>>) {
        var shift = 0
        data.collectIndexed { sliceIndex, slice ->
            sliceLengths[sliceIndex] = slice.toList().size
            slice.collectIndexed { index, value -> permutation[shift + index] = value }
            shift += sliceLengths[sliceIndex]
        }
    }

    override fun getData(): Flow<Flow<Int>> {
        return mapSlice { list -> list }
    }

    override fun checkFormat(): Boolean {
        val contains = BooleanArray(permutation.size) { false }
        var result = true
        permutation.forEach {
            if (it !in permutation.indices || contains[it])
                result = false
            else
                contains[it] = true
        }
        return if (sliceLengths.sum() != salesmanCount)
            false
        else result
    }
}
package hu.bme.thesis.logic.specimen

import kotlinx.coroutines.flow.Flow

data class DOnePartRepresentation(
    override var inUse: Boolean,
    override var costCalculated: Boolean,
    override var cost: Double,
    override var iteration: Int,
    override var orderInPopulation: Int,
    override val objectiveCount: Int,
    override val salesmanCount: Int,
    override val permutationIndices: IntRange,
    override val permutationSize: Int,
    override val sliceLengths: IntArray
) : ISpecimenRepresentation {

    constructor(values: Array<IntArray>): this(
        TODO(),
        TODO(),
        TODO(),
        TODO(),
        TODO(),
        TODO(),
        TODO(),
        TODO(),
        TODO(),
        TODO(),
    )

    constructor(other: DOnePartRepresentation) : this(
        other.inUse,
        other.costCalculated,
        other.cost,
        other.iteration,
        other.orderInPopulation,
        other.objectiveCount,
        other.salesmanCount,
        other.permutationIndices,
        other.permutationSize,
        other.sliceLengths,
    )

    override fun get(index: Int): Int {
        TODO("Not yet implemented")
    }

    override fun set(index: Int, value: Int) {
        TODO("Not yet implemented")
    }

    override fun indexOf(value: Int): Int {
        TODO("Not yet implemented")
    }

    override fun contains(value: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun <T> map(mapper: (value: Int) -> T): Flow<T> {
        TODO("Not yet implemented")
    }

    override fun forEach(operation: (value: Int) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun forEachIndexed(operation: (index: Int, value: Int) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun setEach(operation: (index: Int, value: Int) -> Int) {
        TODO("Not yet implemented")
    }

    override fun <T> mapSlice(mapper: (slice: IntArray) -> T): Collection<T> {
        TODO("Not yet implemented")
    }

    override fun forEachSlice(operation: (slice: IntArray) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun forEachSliceIndexed(operation: (index: Int, slice: IntArray) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun slice(indices: IntRange): Collection<Int> {
        TODO("Not yet implemented")
    }

    override fun shuffle() {
        TODO("Not yet implemented")
    }

    override fun first(selector: (value: Int) -> Boolean): Int {
        TODO("Not yet implemented")
    }

    override fun setData(data: Collection<IntArray>) {
        TODO("Not yet implemented")
    }

    override fun getData(): Collection<IntArray> {
        TODO("Not yet implemented")
    }

    override fun checkFormat(): Boolean {
        TODO("Not yet implemented")
    }

    override fun inverseOfPermutation(): IntArray {
        TODO("Not yet implemented")
    }

    override fun sequentialOfPermutation(): IntArray {
        TODO("Not yet implemented")
    }

    override fun copyOfPermutation(): IntArray {
        TODO("Not yet implemented")
    }

    override fun <T : (Int, (Int) -> Int) -> Collection<Int>> copyOfPermutationBy(initializer: T): Collection<Int> {
        TODO("Not yet implemented")
    }
}
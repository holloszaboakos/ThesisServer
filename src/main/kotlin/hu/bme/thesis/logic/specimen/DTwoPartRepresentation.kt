package hu.bme.thesis.logic.specimen

data class DTwoPartRepresentation(
    val permutation: IntArray,
    val sliceLengths: IntArray,
    override var inUse: Boolean = true,
    override var costCalculated:Boolean = false,
    override var cost: Double = -1.0,
    override var iteration: Int = 0,
    override var orderInPopulation: Int = -1,
) : IRepresentation {

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

    override val size: Int
        get() = permutation.size

    override operator fun get(index: Int) = permutation[index]
    override operator fun set(index: Int, value: Int){
        permutation[index] = value
    }

    override fun indexOf(value: Int): Int = permutation.indexOf(value)

    override fun contains(value: Int): Boolean = permutation.contains(value)

    override fun <T> map(mapper: (Int) -> T): Sequence<T> = permutation.map(mapper).asSequence()

   override fun forEach(operation: (Int) -> Unit) = permutation.forEach(operation)
    override fun forEachIndexed(operation: (Int, Int) -> Unit) = permutation.forEachIndexed(operation)

    override fun setEach(operation: (Int, Int) -> Int){
        permutation.forEachIndexed { index: Int, value: Int -> permutation[index] = operation(index, value) }
    }

    override fun <T> mapSlice(mapper: (slice: Sequence<Int>) -> T): Sequence<T> {
        var geneIndex = 0
        return sequence {
            for (sliceLength in sliceLengths) {
                val slice = permutation.slice(geneIndex until (geneIndex + sliceLength))
                geneIndex += sliceLength
                yield(mapper(slice.asSequence()))
            }
        }
    }

    override fun forEachSlice(operation: (slice: Sequence<Int>) -> Unit) {
        var geneIndex = 0
        sliceLengths.forEach { sliceLength ->
            val slice = permutation.slice(geneIndex until (geneIndex + sliceLength))
            operation(slice.asSequence())
            geneIndex += sliceLength
        }
    }

    override fun forEachSliceIndexed(operation: (index: Int, slice: Sequence<Int>) -> Unit) {
        var geneIndex = 0
        sliceLengths.forEachIndexed { index, sliceLength ->
            val slice = permutation.slice(geneIndex until (geneIndex + sliceLength))
            operation(index, slice.asSequence())
            geneIndex += sliceLength
        }
    }

    override fun slice(indices: IntRange): Sequence<Int> =
        sequence {
            yieldAll(permutation.slice(indices))
        }


    override fun shuffle(){ permutation.shuffle() }
    override fun first(selector: (Int) -> Boolean): Int = permutation.first(selector)

    override fun setData(data: Sequence<Sequence<Int>>) {
            var shift = 0
            data.forEachIndexed { sliceIndex, slice ->
                sliceLengths[sliceIndex] = slice.toList().size
                slice.forEachIndexed { index, value -> permutation[shift + index] = value }
                shift += sliceLengths[sliceIndex]
            }
    }

    override fun getData(): Array<IntArray>{
            return mapSlice { list -> list.toList().toIntArray() }.toList().toTypedArray()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DTwoPartRepresentation

        if (!permutation.contentEquals(other.permutation)) return false
        if (!sliceLengths.contentEquals(other.sliceLengths)) return false
        if (inUse != other.inUse) return false
        if (costCalculated != other.costCalculated) return false
        if (cost != other.cost) return false
        if (iteration != other.iteration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = permutation.contentHashCode()
        result = 31 * result + sliceLengths.contentHashCode()
        result = 31 * result + inUse.hashCode()
        result = 31 * result + costCalculated.hashCode()
        result = 31 * result + cost.hashCode()
        result = 31 * result + iteration.hashCode()
        return result
    }
}
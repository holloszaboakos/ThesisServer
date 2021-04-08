package thesis.core.permutation

import java.math.BigDecimal


data class TwoPartRepresentation(
    private val values: IntArray,
    val sliceLengths: IntArray,
    override var alive: Boolean = true,
    override var cost: BigDecimal = BigDecimal(0),
    override var iteration: Int = 0,
) : IPermutation {

    constructor(data: Array<IntArray>) : this(
        data.let {
            val sum = mutableListOf<Int>()
            data.forEach { sum.addAll(it.toList()) }
            sum.toIntArray()
        },
        data.map { it.size }.toIntArray(),
    )

    override val size: Int
        get() = values.size

    override operator fun get(index: Int) = values[index]
    override operator fun set(index: Int, value: Int) {
        values[index] = value
    }

    override fun indexOf(value: Int): Int = values.indexOf(value)

    override fun contains(value: Int): Boolean = values.contains(value)

    override fun <T> map(mapper: (Int) -> T): List<T> = values.map(mapper)

    override fun forEach(operation: (Int) -> Unit) = values.forEach(operation)
    override fun forEachIndexed(operation: (Int, Int) -> Unit) = values.forEachIndexed(operation)

    override fun setEach(operation: (Int, Int) -> Int) {
        values.forEachIndexed { index: Int, value: Int -> values[index] = value }
    }

    override fun <T> mapSlice(mapper: (List<Int>) -> T): List<T> {
        var geneIndex = 0
        return sliceLengths.map { sliceLength ->
            val slice = values.slice(geneIndex until (geneIndex + sliceLength))
            geneIndex += sliceLength
            mapper(slice)
        }
    }

    override fun forEachSlice(operation: (List<Int>) -> Unit) {
        var geneIndex = 0
        sliceLengths.forEach { sliceLength ->
            val slice = values.slice(geneIndex until (geneIndex + sliceLength))
            operation(slice)
            geneIndex += sliceLength
        }
    }

    override fun forEachSliceIndexed(operation: (Int, List<Int>) -> Unit) {
        var geneIndex = 0
        sliceLengths.forEachIndexed { index, sliceLength ->
            val slice = values.slice(geneIndex until (geneIndex + sliceLength))
            operation(index, slice)
            geneIndex += sliceLength
        }
    }

    override fun slice(indices: IntRange) = values.slice(indices)
    override fun shuffle() = values.shuffle()
    override fun first(selector: (Int) -> Boolean): Int = values.first(selector)

    override fun setData(data: Array<IntArray>) {
        val sum = mutableListOf<Int>()
        data.forEach { sum.addAll(it.toList()) }
        sum.toIntArray().forEachIndexed { index, value ->
            values[index] = value
        }
    }

    override fun getData(): Array<IntArray> {
        return mapSlice { list -> list.toIntArray() }.toTypedArray()
    }

    override fun copy(): IPermutation {
        return this.copy(values = values.clone(), sliceLengths = sliceLengths.clone())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TwoPartRepresentation

        if (!values.contentEquals(other.values)) return false
        if (!sliceLengths.contentEquals(other.sliceLengths)) return false
        if (alive != other.alive) return false
        if (iteration != other.iteration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = values.contentHashCode()
        result = 31 * result + sliceLengths.contentHashCode()
        result = 31 * result + alive.hashCode()
        result = 31 * result + iteration
        return result
    }
}
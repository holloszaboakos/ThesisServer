package thesis.logic.permutation

import java.math.BigDecimal

data class DOnePartRepresentation(
    private val values: IntArray,
    val sliceLengths: IntArray,
    override var alive: Boolean = true,
    override var cost: BigDecimal = BigDecimal(0),
    override var iteration: Int = 0
) : IPermutation<DOnePartRepresentation.Factory> {

    class Factory : IPermutation.IFactory {
        override fun produce(values: Array<IntArray>) = DOnePartRepresentation(values)
    }

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

    override fun <T> map(mapper: (value: Int) -> T): List<T> {
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

    override fun <T> mapSlice(mapper: (slice: List<Int>) -> T): List<T> {
        TODO("Not yet implemented")
    }

    override fun forEachSlice(operation: (slice: List<Int>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun forEachSliceIndexed(operation: (index: Int, slice: List<Int>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun slice(indices: IntRange): List<Int> {
        TODO("Not yet implemented")
    }

    override fun shuffle() {
        TODO("Not yet implemented")
    }

    override fun first(selector: (value: Int) -> Boolean): Int {
        TODO("Not yet implemented")
    }

    override fun setData(data: Array<IntArray>) {
        TODO("Not yet implemented")
    }

    override fun getData(): Array<IntArray> {
        TODO("Not yet implemented")
    }

    override fun copy(): DOnePartRepresentation {
        TODO("Not yet implemented")
    }
}

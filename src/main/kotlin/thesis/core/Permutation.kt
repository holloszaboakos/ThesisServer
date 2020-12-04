package thesis.core

import java.math.BigDecimal

data class Permutation(
    val values: Array<Int>,
    val sliceLengthes: Array<Int>,
    var alive: Boolean,
    var cost: BigDecimal = BigDecimal(0),
    var iteration: Int = 0,
) {
    operator fun get(index: Int) = values[index]
    operator fun set(index: Int, value: Int) {
        values[index] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Permutation

        if (!values.contentEquals(other.values)) return false
        if (!sliceLengthes.contentEquals(other.sliceLengthes)) return false
        if (alive != other.alive) return false
        if (iteration != other.iteration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = values.contentHashCode()
        result = 31 * result + sliceLengthes.contentHashCode()
        result = 31 * result + alive.hashCode()
        result = 31 * result + iteration
        return result
    }
}
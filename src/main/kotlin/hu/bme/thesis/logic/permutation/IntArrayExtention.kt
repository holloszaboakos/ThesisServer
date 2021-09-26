package hu.bme.thesis.logic.permutation

fun IntArray.isPermutation(): Boolean {
    this.sorted().forEachIndexed { index, value ->
        if (index != value)
            return false
    }
    return true
}

fun IntArray.inverse(): IntArray {
    val result = IntArray(size) { it }
    forEachIndexed { index, value ->
        result[value] = index
    }
    return result
}

fun IntArray.sequential(): IntArray {
    val result = IntArray(size) { it }
    inverse().forEachIndexed { value, index ->
        result[index] = this[(index + 1) % size]
    }
    return result
}

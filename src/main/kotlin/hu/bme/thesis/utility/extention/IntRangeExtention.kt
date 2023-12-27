package hu.bme.thesis.utility.extention

fun IntRange.selectRandomPositions(count: Int) =
    shuffled()
        .slice(0 until count)
        .sorted()
        .toIntArray()
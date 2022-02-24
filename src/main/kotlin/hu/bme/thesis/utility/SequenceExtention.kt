package hu.bme.thesis.utility

fun <T> Sequence<T>.slice(range:IntRange):Sequence<T> = sequence {
     var inside = false
    forEachIndexed { index, item ->
        if(!inside && index in range) {
            yield(item)
            inside = true
        }else if(index in range)
            yield(item)
        else if (inside)
            return@forEachIndexed
    }
}

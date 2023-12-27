package hu.bme.thesis.utility.extention

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

fun <T> Flow<T>.slice(range: IntRange): Flow<T> = flow {
    var inside = false
    collectIndexed { index, item ->
        if (!inside && index in range) {
            emit(item)
            inside = true
        } else if (index in range)
            emit(item)
        else if (inside)
            return@collectIndexed
    }
}


fun <T> Flow<T>.shuffled(): Flow<T> {
    return flow {
        toList()
            .asSequence()
            .shuffled()
            .forEach { emit(it) }
    }
}
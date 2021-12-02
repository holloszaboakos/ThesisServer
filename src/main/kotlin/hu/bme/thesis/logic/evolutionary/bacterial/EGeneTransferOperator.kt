package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

enum class EGeneTransferOperator {
    INTO_POSITION {
        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>,
            from: S,
            to: S,
            segmentSize: Int
        ) {
            val randomPosition = Random.nextInt(from.permutationSize - segmentSize)
            val segment = runBlocking { from.slice(randomPosition until randomPosition + segmentSize).toList() }
            val segmentContains = BooleanArray(from.permutationSize) { false }
            segment.forEach { segmentContains[it] = true }
            val toNotInSegment = runBlocking { to.map { it }.filter { !segmentContains[it] }.toList() }
            to.setEach { index, _ ->
                when (index) {
                    in 0 until randomPosition -> toNotInSegment[index]
                    in randomPosition until randomPosition + segmentSize -> segment[index - randomPosition]
                    in randomPosition + segmentSize until to.permutationSize -> toNotInSegment[index - segmentSize]
                    else -> throw Error("UnhandledCase")
                }
            }
            to.iteration = alg.iteration
            to.costCalculated = false
        }

    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        alg: BacterialAlgorithm<S>,
        from: S,
        to: S,
        segmentSize: Int
    )
}
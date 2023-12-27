package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.utility.extention.nextSegmentStartPosition
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

enum class EGeneTransferOperator {
    INTO_POSITION {
        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>,
            from: S,
            to: S
        ) {
            alg.run {
                val randomPosition =
                    Random.nextSegmentStartPosition(from.permutationIndices.count(), geneTransferSegmentLength)
                val segment =
                    runBlocking { from.slice(randomPosition until randomPosition + geneTransferSegmentLength).toList() }
                val segmentContains = BooleanArray(from.permutationIndices.count()) { false }
                segment.forEach { segmentContains[it] = true }
                val toNotInSegment = runBlocking { to.map { it }.filter { !segmentContains[it] }.toList() }
                to.setEach { index, _ ->
                    when (index) {
                        in 0 until randomPosition -> toNotInSegment[index]
                        in randomPosition until randomPosition + geneTransferSegmentLength -> segment[index - randomPosition]
                        in randomPosition + geneTransferSegmentLength..to.permutationIndices.last -> toNotInSegment[index - geneTransferSegmentLength]
                        else -> throw Error("UnhandledCase")
                    }
                }
                to.iteration = alg.iteration
                to.costCalculated = false
                if(!to.checkFormat()){
                    println("Wrongly formatted")
                }
            }
        }

    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        alg: BacterialAlgorithm<S>,
        from: S,
        to: S
    )
}
package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import  hu.bme.thesis.utility.*
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.runBlocking

enum class EProduceMutant {
    SHUFFLE {
        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>,
            specimen: S,
            segmentPosition: Int,
            segmentSize: Int
        ): S {
            alg.run {
                return permutationFactory.copy(specimen)
                    .also { clone ->
                        runBlocking {
                            clone.slice(segmentPosition until (segmentPosition + segmentSize))
                                .shuffled()
                                .collectIndexed { index, value ->
                                    clone[segmentPosition + index] = value
                                }
                        }
                    }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        alg: BacterialAlgorithm<S>,
        specimen: S,
        segmentPosition: Int,
        segmentSize: Int
    ): S
}

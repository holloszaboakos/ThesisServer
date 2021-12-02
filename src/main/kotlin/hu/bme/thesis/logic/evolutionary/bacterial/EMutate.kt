package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.utility.slice
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

enum class EMutate {
    DEFAULT {
        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>,
            cloneCount: Int,
            segmentSize: Int
        ) {
            runBlocking {
                alg.run {
                    population.asSequence()
                        .slice(1 until population.size)
                        .forEach { specimen ->
                            repeat(100) {
                                mutateSpecimen(specimen, cloneCount, segmentSize)
                            }
                            specimen.costCalculated = false
                        }
                }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        alg: BacterialAlgorithm<S>,
        cloneCount: Int,
        segmentSize: Int
    )
}
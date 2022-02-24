package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.utility.slice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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
                        .map { specimen ->
                            launch(Dispatchers.Default) {
                                repeat(100) {
                                    mutateSpecimen(specimen, cloneCount, segmentSize)
                                }
                                specimen.costCalculated = false
                            }
                        }.forEach { it.join() }
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
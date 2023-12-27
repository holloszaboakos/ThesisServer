package hu.bme.thesis.logic.common.lifecycle

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.evolutionary.SEvolutionaryAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.runBlocking
import javax.xml.bind.TypeConstraintException

enum class EClear {
    EVOLUTIONARY {
        override fun <S : ISpecimenRepresentation> invoke(alg: AAlgorithm4VRP<S>) {
            when (alg) {
                is SEvolutionaryAlgorithm<S> ->
                    when (alg.state) {
                        AAlgorithm4VRP.State.RESUMED -> {
                            runBlocking { alg.pause() }
                            alg.clear()
                        }
                        AAlgorithm4VRP.State.INITIALIZED -> {
                            alg.iteration = 0
                            alg.population.forEach { it.iteration = 0 }
                            alg.state = AAlgorithm4VRP.State.CREATED
                        }
                        else -> {
                        }
                    }
                else -> throw TypeConstraintException("Genetic algorithm was expected")
            }
        }
    },
    DEFAULT {
        override fun <S : ISpecimenRepresentation> invoke(alg: AAlgorithm4VRP<S>) {
            when (alg.state) {
                AAlgorithm4VRP.State.RESUMED -> {
                    runBlocking { alg.pause() }
                    alg.clear()
                }
                AAlgorithm4VRP.State.INITIALIZED -> {
                    alg.state = AAlgorithm4VRP.State.CREATED
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: AAlgorithm4VRP<S>)
}
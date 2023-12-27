package hu.bme.thesis.logic.common.lifecycle

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import javax.xml.bind.TypeConstraintException

enum class EInitialize {
    EVOLUTIONARY {
        override fun <S : ISpecimenRepresentation> invoke(alg: AAlgorithm4VRP<S>) {
            when (alg) {
                is GeneticAlgorithm<S> ->
                    alg.run {
                        when (state) {
                            AAlgorithm4VRP.State.CREATED -> {
                                initializePopulation()
                                orderByCost()
                                boost()
                                best = subSolutionFactory.copy(population.first())
                                worst = subSolutionFactory.copy(population.last())
                                state = AAlgorithm4VRP.State.INITIALIZED
                            }
                            else -> {
                            }
                        }
                    }
                is BacterialAlgorithm<S> ->
                    alg.run {
                        when (state) {
                            AAlgorithm4VRP.State.CREATED -> {
                                println("initializePopulation")
                                initializePopulation()
                                println("orderByCost")
                                orderByCost()
                                println("orderedByCost")
                                best = subSolutionFactory.copy(population.first())
                                worst = subSolutionFactory.copy(population.last())
                                state = AAlgorithm4VRP.State.INITIALIZED
                            }
                            else -> {
                            }
                        }
                    }
                else -> throw TypeConstraintException("Genetic algorithm was expected!")
            }
        }
    },
    DEFAULT {
        override fun <S : ISpecimenRepresentation> invoke(alg: AAlgorithm4VRP<S>) = runBlocking {
            alg.run {
                when (state) {
                    AAlgorithm4VRP.State.CREATED -> {
                        state = AAlgorithm4VRP.State.INITIALIZED
                    }
                    else -> {
                    }
                }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: AAlgorithm4VRP<S>)
}
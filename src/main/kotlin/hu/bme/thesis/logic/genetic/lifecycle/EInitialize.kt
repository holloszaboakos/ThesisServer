package hu.bme.thesis.logic.genetic.lifecycle

import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation

enum class EInitialize {
    STANDARD {
        override fun <S : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>) = runBlocking {
            alg.run {
                when (state) {
                    DGeneticAlgorithm.State.CREATED -> {
                        initializePopulation()
                        orderByCost()
                        boost()
                        best = permutationFactory.copy(population.first())
                        worst = permutationFactory.copy(population.last())
                        state = DGeneticAlgorithm.State.INITIALIZED
                    }
                    else -> {
                    }
                }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>)
}
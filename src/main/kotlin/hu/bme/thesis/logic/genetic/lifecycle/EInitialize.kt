package hu.bme.thesis.logic.genetic.lifecycle

import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation

enum class EInitialize {
    STANDARD {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
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

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>)
}
package hu.bme.thesis.logic.genetic.control

import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation

enum class EIteration {
    STANDARD {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
            alg.run {
                state = DGeneticAlgorithm.State.RESUMED
                selection()
                crossover()
                mutate()
                orderByCost()
                boost()
                best = permutationFactory.copy(population.first())
                worst = permutationFactory.copy(population.last())
                iteration++
                state = DGeneticAlgorithm.State.INITIALIZED
            }
        }
    };

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>)
}
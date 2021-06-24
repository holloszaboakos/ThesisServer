package hu.bme.thesis.logic.genetic.control

import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.permutation.IPermutation

enum class EIteration {
    STANDARD {
        override fun <P : IPermutation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking{
            alg.state = DGeneticAlgorithm.State.RESUMED
            alg.selection()
            alg.crossover()
            alg.mutate()
            alg.orderByCost()
            alg.boost()
            alg.best = alg.population.first().copy() as P
            alg.worst = alg.population.last().copy() as P
            alg.iteration++
            alg.state = DGeneticAlgorithm.State.INITIALIZED
        }
    };

    abstract operator fun <P : IPermutation> invoke(alg: DGeneticAlgorithm<P>)
}
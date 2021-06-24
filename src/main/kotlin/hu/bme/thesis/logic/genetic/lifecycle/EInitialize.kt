package hu.bme.thesis.logic.genetic.lifecycle

import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.permutation.IPermutation

enum class EInitialize {
    STANDARD {
        override fun <P : IPermutation>
                invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
            when (alg.state) {
                DGeneticAlgorithm.State.CREATED -> {
                    alg.initializePopulation()
                    alg.orderByCost()
                    alg.boost()
                    alg.best = alg.population.first().copy() as P
                    alg.worst = alg.population.last().copy() as P
                    alg.state = DGeneticAlgorithm.State.INITIALIZED
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun <P : IPermutation> invoke(alg: DGeneticAlgorithm<P>)
}
package thesis.logic.genetic.lifecycle

import kotlinx.coroutines.runBlocking
import thesis.logic.genetic.DGeneticAlgorithm
import thesis.logic.permutation.IPermutation

enum class EInitialize {
    STANDARD {
        override fun <P : IPermutation<out IPermutation.IFactory>>
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

    abstract operator fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>)
}
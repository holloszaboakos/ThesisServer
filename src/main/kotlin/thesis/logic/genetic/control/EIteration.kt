package thesis.logic.genetic.control

import kotlinx.coroutines.runBlocking
import thesis.logic.genetic.DGeneticAlgorithm
import thesis.logic.permutation.IPermutation

enum class EIteration {
    STANDARD {
        override fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>) = runBlocking{
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

    abstract operator fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>)
}
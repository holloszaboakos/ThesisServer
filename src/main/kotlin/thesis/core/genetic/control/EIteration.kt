package thesis.core.genetic.control

import kotlinx.coroutines.runBlocking
import thesis.core.genetic.GeneticAlgorithm
import thesis.core.permutation.EPermutationFactory
import thesis.core.permutation.TwoPartRepresentation
import java.lang.System.currentTimeMillis

enum class EIteration {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) = runBlocking{
            alg.state = GeneticAlgorithm.State.RESUMED
            alg.selection()
            alg.crossover()
            alg.mutate()
            alg.orderByCost()
            alg.boost()
            alg.best = alg.population.first().copy()
            alg.worst = alg.population.last().copy()
            alg.iteration++
            alg.state = GeneticAlgorithm.State.INITIALIZED
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
package thesis.core.genetic.lifecycle

import kotlinx.coroutines.runBlocking
import thesis.core.genetic.GeneticAlgorithm
import thesis.core.permutation.EPermutationFactory
import thesis.core.permutation.TwoPartRepresentation
import kotlin.random.Random
import kotlin.random.nextInt

enum class EInitialize {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) = runBlocking{
            when (alg.state) {
                GeneticAlgorithm.State.CREATED -> {
                    alg.initializePopulation()
                    alg.orderByCost()
                    alg.boost()
                    alg.best = alg.population.first().copy()
                    alg.worst = alg.population.last().copy()
                    alg.state = GeneticAlgorithm.State.INITIALIZED
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
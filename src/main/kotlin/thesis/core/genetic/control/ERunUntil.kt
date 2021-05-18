package thesis.core.genetic.control

import kotlinx.coroutines.runBlocking
import thesis.core.genetic.GeneticAlgorithm

enum class ERunUntil {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.state = GeneticAlgorithm.State.RESUMED
            while (
                alg.runTime_Second < alg.timeLimit
                && alg.iteration < alg.iterationLimit
                && alg.state == GeneticAlgorithm.State.RESUMED
            ) runBlocking {
                alg.selection()
                alg.crossover()
                alg.mutate()
                alg.orderByCost()
                alg.boost()
                alg.best = alg.population.first().copy()
                alg.worst = alg.population.last().copy()
                alg.iteration++
            }
            alg.state = GeneticAlgorithm.State.INITIALIZED
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
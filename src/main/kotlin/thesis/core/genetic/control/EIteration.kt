package thesis.core.genetic.control

import thesis.core.genetic.GeneticAlgorithm
import java.lang.System.currentTimeMillis

enum class EIteration {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.state = GeneticAlgorithm.State.RESUMED
            alg.resumeTime = currentTimeMillis()
            alg.selection()
            alg.crossover()
            alg.mutate()
            alg.orderByCost()
            alg.boost()
            alg.iteration++
            alg.spentTime += currentTimeMillis() - alg.resumeTime
            alg.state = GeneticAlgorithm.State.STARTED
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
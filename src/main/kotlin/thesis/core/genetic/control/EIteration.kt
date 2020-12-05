package thesis.core.genetic.control

import thesis.core.genetic.GeneticAlgorithm
import java.lang.System.currentTimeMillis

enum class EIteration {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.state = GeneticAlgorithm.State.RESUMED
            alg.selection()
            alg.crossover()
            alg.mutate()
            alg.orderByCost()
            alg.boost()
            alg.best = alg.population.first().copy(
                values = alg.population.first().values.clone(),
                sliceLengthes = alg.population.first().sliceLengthes.clone()
            )
            alg.worst = alg.population.last().copy(
                values = alg.population.last().values.clone(),
                sliceLengthes = alg.population.last().sliceLengthes.clone()
            )
            alg.iteration++
            alg.state = GeneticAlgorithm.State.INITIALIZED
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
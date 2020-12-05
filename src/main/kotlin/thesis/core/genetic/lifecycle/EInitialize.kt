package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm

enum class EInitialize {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            when (alg.state) {
                GeneticAlgorithm.State.CREATED -> {
                    alg.initializePopulation()
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
                    alg.state = GeneticAlgorithm.State.INITIALIZED
                }
                else -> {}
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
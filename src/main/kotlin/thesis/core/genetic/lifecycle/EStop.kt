package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm

enum class EStop {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            when (alg.state) {
                GeneticAlgorithm.State.RESUMED -> {
                    alg.pause()
                    alg.stop()
                }
                GeneticAlgorithm.State.STARTED -> {
                    alg.iteration = 0
                    alg.spentTime = 0
                    alg.population.forEach { it.iteration = 0 }
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
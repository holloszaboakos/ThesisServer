package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm

enum class EClear {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            when (alg.state) {
                GeneticAlgorithm.State.RESUMED -> {
                    alg.pause()
                    alg.clear()
                }
                GeneticAlgorithm.State.INITIALIZED -> {
                    alg.iteration = 0
                    alg.population.forEach { it.iteration = -1 }
                    alg.state = GeneticAlgorithm.State.CREATED
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
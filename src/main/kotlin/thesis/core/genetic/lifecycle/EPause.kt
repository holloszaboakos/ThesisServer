package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm

enum class EPause {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            when (alg.state) {
                GeneticAlgorithm.State.RESUMED -> {
                    alg.state = GeneticAlgorithm.State.STARTED
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
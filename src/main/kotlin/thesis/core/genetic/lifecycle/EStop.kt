package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm

enum class EStop {
    EXAMPLE {
        override fun invoke(alg: GeneticAlgorithm) {
            TODO("Not yet implemented")
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
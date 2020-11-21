package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm

enum class ESelectSurvivers {
    RANDOM {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.population
                .slice(0 until alg.population.size / 4)
                .forEach { it.alive = true }
            alg.population
                .slice(alg.population.size / 4 until alg.population.size)
                .shuffled()
                .slice(0 until alg.population.size / 4)
                .forEach { it.alive = true }
        }
    }
    ;

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
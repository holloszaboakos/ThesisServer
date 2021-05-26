package thesis.logic.genetic.steps

import thesis.logic.genetic.DGeneticAlgorithm

enum class ESelectSurvivers {
    RANDOM {
        override fun invoke(alg: DGeneticAlgorithm<*>) {
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

    abstract operator fun invoke(alg: DGeneticAlgorithm<*>)
}
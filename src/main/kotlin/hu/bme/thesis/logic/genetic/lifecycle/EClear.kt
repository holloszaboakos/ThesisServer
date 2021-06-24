package hu.bme.thesis.logic.genetic.lifecycle

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm

enum class EClear {
    STANDARD {
        override fun invoke(alg: DGeneticAlgorithm<*>) {
            when (alg.state) {
                DGeneticAlgorithm.State.RESUMED -> {
                    alg.pause()
                    alg.clear()
                }
                DGeneticAlgorithm.State.INITIALIZED -> {
                    alg.iteration = 0
                    alg.population.forEach { it.iteration = -1 }
                    alg.state = DGeneticAlgorithm.State.CREATED
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: DGeneticAlgorithm<*>)
}
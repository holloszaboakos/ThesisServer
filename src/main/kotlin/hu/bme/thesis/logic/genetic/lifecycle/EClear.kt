package hu.bme.thesis.logic.genetic.lifecycle

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation

enum class EClear {
    STANDARD {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {
            when (alg.state) {
                DGeneticAlgorithm.State.RESUMED -> {
                    alg.pause()
                    alg.clear()
                }
                DGeneticAlgorithm.State.INITIALIZED -> {
                    alg.iteration = 0
                    alg.population.forEach { it.iteration = 0 }
                    alg.state = DGeneticAlgorithm.State.CREATED
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>)
}
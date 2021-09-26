package hu.bme.thesis.logic.genetic.lifecycle

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation

enum class EPause {
    STANDARD {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {
            when (alg.state) {
                DGeneticAlgorithm.State.RESUMED -> {
                    alg.state = DGeneticAlgorithm.State.INITIALIZED
                    alg.thread?.join()
                    alg.thread = null
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun <P : IRepresentation>invoke(alg: DGeneticAlgorithm<P>)
}
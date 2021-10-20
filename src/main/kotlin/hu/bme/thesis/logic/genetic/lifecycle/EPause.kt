package hu.bme.thesis.logic.genetic.lifecycle

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation

enum class EPause {
    STANDARD {
        override fun <S : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>) {
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

    abstract operator fun <S : ISpecimenRepresentation>invoke(alg: DGeneticAlgorithm<S>)
}
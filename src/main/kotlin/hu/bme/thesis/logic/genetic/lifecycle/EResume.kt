package hu.bme.thesis.logic.genetic.lifecycle

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlin.concurrent.thread

enum class EResume {
    STANDARD {
        override fun <S : ISpecimenRepresentation>invoke(alg: DGeneticAlgorithm<S>) {
            when (alg.state) {
                DGeneticAlgorithm.State.CREATED -> {
                    alg.initialize()
                    alg.resume()
                }
                DGeneticAlgorithm.State.INITIALIZED -> {
                    alg.thread = thread{
                        alg.state = DGeneticAlgorithm.State.RESUMED
                        alg.run()
                    }
                }
                else -> {
                }
            }
        }
    };

    abstract operator  fun <S : ISpecimenRepresentation>invoke(alg: DGeneticAlgorithm<S>)
}
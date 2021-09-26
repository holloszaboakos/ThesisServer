package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation
import hu.bme.thesis.utility.slice

enum class ESelectSurvivors {
    RANDOM {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>): () -> Unit {
            return  {
                alg.run {
                    population.asSequence()
                        .slice(0 until population.size / 4)
                        .forEach { it.inUse = true }
                    population.asSequence()
                        .slice(population.size / 4 until population.size)
                        .shuffled()
                        .slice(0 until population.size / 4)
                        .forEach { it.inUse = true }
                }
            }
        }
    }
    ;

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>): () -> Unit
}
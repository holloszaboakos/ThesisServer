package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.utility.slice

enum class ESelectSurvivors {
    RANDOM {
        override fun <P : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<P>): () -> Unit {
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

    abstract operator fun <P : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<P>): () -> Unit
}
package hu.bme.thesis.logic.evolutionary.genetic

import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.utility.extention.slice

enum class ESelectSurvivors {
    RANDOM {
        override fun <S : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>) {
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
    ;

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>)
}
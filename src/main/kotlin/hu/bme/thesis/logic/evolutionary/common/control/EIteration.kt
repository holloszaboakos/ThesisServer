package hu.bme.thesis.logic.evolutionary.common.control

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.evolutionary.SEvolutionaryAlgorithm
import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation

enum class EIteration {
    DEFAULT {
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>, manageLifeCycle: Boolean) {
            alg.run {
                if (manageLifeCycle)
                    state = AAlgorithm4VRP.State.RESUMED
                when (this) {
                    is GeneticAlgorithm<S> -> {
                        selection()
                        crossover()
                        mutate()
                        orderByCost()
                        boost()
                    }
                    is BacterialAlgorithm<S> -> {

                        println("geneTransfer")
                        geneTransfer()
                        println("mutate")
                        mutate()
                        if (iteration % 50 == 0) {
                            println("boost")
                            boost()
                        }
                        println("orderByCost")
                        orderByCost()
                    }
                }
                best = subSolutionFactory.copy(population.first())
                worst = subSolutionFactory.copy(population.last())
                iteration++
                if (manageLifeCycle)
                    state = AAlgorithm4VRP.State.INITIALIZED
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>, manageLifeCycle: Boolean)
}
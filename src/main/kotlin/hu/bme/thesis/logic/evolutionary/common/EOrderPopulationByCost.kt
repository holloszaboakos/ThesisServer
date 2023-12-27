package hu.bme.thesis.logic.evolutionary.common

import hu.bme.thesis.logic.evolutionary.SEvolutionaryAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class EOrderPopulationByCost {
    RECALC_ALL {
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            alg.run {
                population.asSequence()
                    .filter { !it.costCalculated }
                    .map { specimen ->
                        cost(specimen)
                    }.toList()
                population.sortBy { it.cost }
                population.asSequence()
                    .forEachIndexed { index, it ->
                        it.orderInPopulation = index
                        if (it.cost == 0.0)
                            println("Impossible!")
                        it.inUse = false
                    }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>)
}
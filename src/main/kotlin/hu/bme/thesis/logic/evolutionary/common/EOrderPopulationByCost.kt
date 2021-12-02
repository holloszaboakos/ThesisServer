package hu.bme.thesis.logic.evolutionary.common

import hu.bme.thesis.logic.evolutionary.SEvolutionaryAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import io.ktor.utils.io.*
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

enum class EOrderPopulationByCost {
    RECALC_ALL {
        @OptIn(DangerousInternalIoApi::class)
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            alg.run {
                runBlocking {
                        population.asFlow()
                            .filter { !it.costCalculated }
                            .buffer(100)
                            .collect { specimen ->
                                cost(specimen)
                            }
                }
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
package hu.bme.thesis.logic.evolutionary.common.control

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.evolutionary.SEvolutionaryAlgorithm
import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

enum class ECycle {
    DEFAULT {
        @OptIn(ExperimentalTime::class)
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            alg.run {
                val oldIterationCount = iteration
                state = AAlgorithm4VRP.State.RESUMED
                while (
                    runTime_Second < timeLimit
                    && iteration < iterationLimit
                    && iteration - oldIterationCount < costGraph.objectives.size
                ) runBlocking {
                    val time = measureTime {
                        iterate(false)
                    }
                    println("$iteration. iteration: bestCost: ${best?.cost} worstCost: ${worst?.cost} runtime: ${time.inWholeSeconds}s")
                }
                state = AAlgorithm4VRP.State.INITIALIZED
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>)
}
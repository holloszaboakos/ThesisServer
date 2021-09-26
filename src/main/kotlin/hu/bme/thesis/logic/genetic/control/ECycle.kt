package hu.bme.thesis.logic.genetic.control

import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

enum class ECycle {
    STANDARD {
        @OptIn(ExperimentalTime::class)
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {
            alg.run {
                val oldIterationCount = iteration
                state = DGeneticAlgorithm.State.RESUMED
                while (
                    runTime_Second < timeLimit
                    && iteration < iterationLimit
                    && iteration - oldIterationCount < objectives.size
                ) runBlocking {
                    val time = measureTime {
                        selection()
                        crossover()
                        mutate()
                        orderByCost()
                        boost()
                        best = permutationFactory.copy(population.first())
                        worst = permutationFactory.copy(population.last())
                        iteration++
                    }
                    println("$iteration. iteration: bestCost: ${best?.cost} worstCost: ${worst?.cost} runtime: ${time.inWholeSeconds}s")
                }
                state = DGeneticAlgorithm.State.INITIALIZED
            }
        }
    };

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>)
}
package hu.bme.thesis.logic.genetic.steps

import kotlinx.coroutines.coroutineScope
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class EOrderPopulationByCost {
    RECALC_ALL {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
            alg.run {
                val jobCount = 100
                val jobs: Array<Job?> = Array(jobCount) { null }
                population.asSequence()
                    .filter { !it.costCalculated }
                    .forEachIndexed { index, p ->
                        jobs[index % jobCount]?.join()
                        jobs[index % jobCount] =
                            launch {
                                alg.cost(p)
                            }
                    }
                jobs.forEach { it?.join() }
                population = ArrayList(
                    population.asSequence()
                        .sortedBy { it.cost }
                        .mapIndexed { index, it ->
                            it.orderInPopulation = index
                            if (it.cost == 0.0)
                                println("Impossible!")
                            it.inUse = false
                            it
                        }.toList()
                )
            }
        }
    };

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>)
}
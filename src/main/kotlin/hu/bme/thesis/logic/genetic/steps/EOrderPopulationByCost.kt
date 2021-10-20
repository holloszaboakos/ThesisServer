package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class EOrderPopulationByCost {
    RECALC_ALL {
        override fun <S : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>): Unit = runBlocking {
            alg.run {
                val jobCount = 100
                val jobs: Array<Job?> = Array(jobCount) { null }
                population.asSequence()
                    .filter { !it.costCalculated }
                    .forEachIndexed { index, specimen ->
                        jobs[index % jobCount]?.join()
                        jobs[index % jobCount] =
                            launch {
                                alg.cost(specimen)
                            }
                    }
                jobs.forEach { it?.join() }
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

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>)
}
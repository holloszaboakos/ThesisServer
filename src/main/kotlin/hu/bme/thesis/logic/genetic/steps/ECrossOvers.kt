package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class ECrossOvers {
    ORDERED {
        override fun <S : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>) = runBlocking {
            val children = alg.population.filter { !it.inUse }
            val parent = alg.population.filter { it.inUse }//.shuffled()
            val jobCount = 100
            val jobs: Array<Job?> = Array(jobCount) { null }
            parent.forEachIndexed { index, primerParent ->
                jobs[index % jobCount]?.join()
                jobs[index % jobCount] =
                    launch {
                        val seconderParent =
                            if (index % 2 == 0)
                                parent[index + 1]
                            else
                                parent[index - 1]
                        alg.crossoverOperator(Pair(primerParent, seconderParent), children[index])
                    }
            }
            jobs.forEach { it?.join() }

        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>)
}
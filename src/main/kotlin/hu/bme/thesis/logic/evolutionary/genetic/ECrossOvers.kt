package hu.bme.thesis.logic.evolutionary.genetic

import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.*

enum class ECrossOvers {
    ORDERED {
        override fun <S : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>) {
            val children = alg.population.filter { !it.inUse }
            val parent = alg.population.filter { it.inUse }.shuffled()

            runBlocking {
                val jobs = parent.mapIndexed { index, primerParent ->
                        launch(Dispatchers.Default) {
                            val seconderParent =
                                if (index % 2 == 0)
                                    parent[index + 1]
                                else
                                    parent[index - 1]
                            alg.crossoverOperator(Pair(primerParent, seconderParent), children[index])
                        }
                    }
                jobs.forEach{it.join()}
            }

        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>)
}
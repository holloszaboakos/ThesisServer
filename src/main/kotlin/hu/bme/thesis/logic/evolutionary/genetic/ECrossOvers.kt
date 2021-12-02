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
                coroutineScope {
                    parent.forEachIndexed { index, primerParent ->
                        launch(Dispatchers.Default) {
                            val seconderParent =
                                if (index % 2 == 0)
                                    parent[index + 1]
                                else
                                    parent[index - 1]
                            alg.crossoverOperator(Pair(primerParent, seconderParent), children[index])
                        }
                    }
                }
            }

        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>)
}
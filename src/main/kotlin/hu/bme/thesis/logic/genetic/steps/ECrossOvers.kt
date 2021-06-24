package hu.bme.thesis.logic.genetic.steps

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.permutation.IPermutation

enum class ECrossOvers {
    ORDERED {
        override suspend fun <P : IPermutation> invoke(alg: DGeneticAlgorithm<P>) = coroutineScope {
            val children = alg.population.filter { !it.alive }
            val parent = alg.population.filter { it.alive }.shuffled()
            parent.forEachIndexed { index, primerParent ->
                launch {
                    val seconderParent =
                        if (index % 2 == 0)
                            parent[index + 1]
                        else
                            parent[index - 1]
                    alg.crossoverOperator(Pair(primerParent, seconderParent), children[index])
                }
            }

        }
    };

    abstract suspend operator fun <P : IPermutation> invoke(alg: DGeneticAlgorithm<P>)
}
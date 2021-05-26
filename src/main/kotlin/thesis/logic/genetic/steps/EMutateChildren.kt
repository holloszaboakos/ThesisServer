package thesis.logic.genetic.steps

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import thesis.logic.genetic.DGeneticAlgorithm
import kotlin.random.Random
import kotlin.random.nextInt

enum class EMutateChildren {
    SWAP {
        override suspend fun invoke(alg: DGeneticAlgorithm<*>) = coroutineScope {
            if (alg.objectives.size > 1)
                alg.population
                    .filter { it.iteration == alg.iteration }
                    .shuffled()
                    .slice(0..alg.population.size / 4)
                    .forEach { child ->
                        launch {
                            val firstCutIndex = Random.nextInt(alg.objectives.indices)
                            val secondCutIndex = Random.nextInt(alg.objectives.indices)
                                .let {
                                    if (it == firstCutIndex)
                                        (it + 1) % alg.objectives.size
                                    else
                                        it
                                }

                            val tmp = child[firstCutIndex]
                            child[firstCutIndex] = child[secondCutIndex]
                            child[secondCutIndex] = tmp
                        }
                    }
        }
    },

    REVERSE {
        override suspend fun invoke(alg: DGeneticAlgorithm<*>) = coroutineScope {
            if (alg.objectives.size > 1)
                alg.population
                    .filter { it.iteration == alg.iteration }
                    .shuffled()
                    .slice(0..(alg.population.size / 4))
                    .forEach { child ->
                        launch {
                            val firstCutIndex = Random.nextInt(alg.objectives.indices)
                            val secondCutIndex = Random.nextInt(alg.objectives.indices)
                                .let {
                                    if (it == firstCutIndex)
                                        (it + 1) % alg.objectives.size
                                    else
                                        it
                                }

                            if (secondCutIndex > firstCutIndex) {
                                val reversed = child.slice(firstCutIndex..secondCutIndex).reversed()
                                for (geneIndex in firstCutIndex..secondCutIndex)
                                    child[geneIndex] = reversed[geneIndex - firstCutIndex]
                            } else {
                                val reversed = child.slice(secondCutIndex..firstCutIndex).reversed()
                                for (geneIndex in secondCutIndex..firstCutIndex)
                                    child[geneIndex] = reversed[geneIndex - secondCutIndex]
                            }
                        }
                    }
        }
    };

    abstract suspend operator fun invoke(alg: DGeneticAlgorithm<*>)
}
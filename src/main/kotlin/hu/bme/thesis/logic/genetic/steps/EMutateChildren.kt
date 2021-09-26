package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation
import hu.bme.thesis.utility.slice
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.random.nextInt

enum class EMutateChildren {

    RESET {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
            val basePermutation = List(alg.best?.size ?: 0) { it }.shuffled().toIntArray()
            if (alg.objectives.size > 1)
                alg.population.asSequence()
                    .filter { it.iteration == alg.iteration }
                    .shuffled()
                    .slice(0..( alg.population.size / 16))
                    .forEachIndexed { instanceIndex, child ->
                        if (instanceIndex < child.size) {
                            val step = instanceIndex % (child.size - 1) + 1
                            if (step == 1) {
                                basePermutation.shuffle()
                            }
                            val newContains = BooleanArray(child.size){false}
                            val newPermutation = IntArray(child.size) { -1 }
                            var baseIndex = step
                            for (newIndex in 0 until child.size) {
                                if (newContains[basePermutation[baseIndex]])
                                    baseIndex = (baseIndex + 1) % child.size
                                newPermutation[newIndex] = basePermutation[baseIndex]
                                newContains[basePermutation[baseIndex]] = true
                                baseIndex = (baseIndex + step) % child.size
                            }

                            val breakPoints = newPermutation
                                .mapIndexed { index, value ->
                                    if (value < child.size)
                                        -1
                                    else
                                        index
                                }
                                .filter { it != -1 }
                                .toMutableList()

                            breakPoints.add(0, -1)
                            breakPoints.add(child.size)
                            var it = -1
                            child.setData(sequence {
                                it++
                                newPermutation.slice((breakPoints[it] + 1) until breakPoints[it + 1])

                            })
                            child.iteration = 0
                            child.costCalculated = false
                            child.inUse = true
                            child.cost = 0.0
                        }
                    }
        }
    },
    SWAP {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
            if (alg.objectives.size > 1)
                alg.population.asSequence()
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
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
            if (alg.objectives.size > 1)
                alg.population.asSequence()
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
                                val reversed = child.slice(firstCutIndex..secondCutIndex).toList().reversed()
                                for (geneIndex in firstCutIndex..secondCutIndex)
                                    child[geneIndex] = reversed[geneIndex - firstCutIndex]
                            } else {
                                val reversed = child.slice(secondCutIndex..firstCutIndex).toList().reversed()
                                for (geneIndex in secondCutIndex..firstCutIndex)
                                    child[geneIndex] = reversed[geneIndex - secondCutIndex]
                            }
                        }
                    }
        }
    },REVERSE_OR_RESET{
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) = runBlocking {
            if(alg.iteration % 100 == 0)
                RESET(alg)
            else
                REVERSE(alg)
        }
    };

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>)
}
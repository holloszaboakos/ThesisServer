package hu.bme.thesis.logic.evolutionary.genetic

import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.utility.extention.slice
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.random.nextInt

enum class EMutateChildren {

    RESET {
        override fun <S  : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>) = runBlocking {
            val basePermutation = List(alg.best?.permutationIndices?.count() ?: 0) { it }.shuffled().toIntArray()
            if (alg.costGraph.objectives.size > 1)
                alg.population.asSequence()
                    .filter { it.iteration == alg.iteration }
                    .shuffled()
                    .slice(0 until ( alg.population.size / 16))
                    .forEachIndexed { instanceIndex, child ->
                        if (instanceIndex < child.permutationIndices.count()) {
                            val step = instanceIndex % (child.permutationIndices.count() - 1) + 1
                            if (step == 1) {
                                basePermutation.shuffle()
                            }
                            val newContains = BooleanArray(child.permutationIndices.count()){false}
                            val newPermutation = IntArray(child.permutationIndices.count()) { -1 }
                            var baseIndex = step
                            for (newIndex in 0 until child.permutationIndices.count()) {
                                if (newContains[basePermutation[baseIndex]])
                                    baseIndex = (baseIndex + 1) % child.permutationIndices.count()
                                newPermutation[newIndex] = basePermutation[baseIndex]
                                newContains[basePermutation[baseIndex]] = true
                                baseIndex = (baseIndex + step) % child.permutationIndices.count()
                            }

                            val breakPoints = newPermutation
                                .mapIndexed { index, value ->
                                    if (value < child.permutationIndices.count())
                                        -1
                                    else
                                        index
                                }
                                .filter { it != -1 }
                                .toMutableList()

                            breakPoints.add(0, -1)
                            breakPoints.add(child.permutationIndices.count())
                            child.setData(List(breakPoints.size - 1) {
                                newPermutation.slice((breakPoints[it] + 1) until breakPoints[it + 1])
                                    .toIntArray()

                            })

                            if (!child.checkFormat())
                                throw Error("Invalid specimen!")
                        }
                    }
        }
    },
    SWAP {
        override fun <S  : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>) = runBlocking {
            if (alg.costGraph.objectives.size > 1)
                alg.population.asSequence()
                    .filter { it.iteration == alg.iteration }
                    .shuffled()
                    .slice(0 until alg.population.size / 4)
                    .forEach { child ->
                        launch {
                            val firstCutIndex = Random.nextInt(alg.costGraph.objectives.indices)
                            val secondCutIndex = Random.nextInt(alg.costGraph.objectives.indices)
                                .let {
                                    if (it == firstCutIndex)
                                        (it + 1) % alg.costGraph.objectives.size
                                    else
                                        it
                                }

                            val tmp = child[firstCutIndex]
                            child[firstCutIndex] = child[secondCutIndex]
                            child[secondCutIndex] = tmp
                        }
                        if (!child.checkFormat())
                            throw Error("Invalid specimen!")
                    }

        }
    },

    REVERSE {
        override fun <S  : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>) = runBlocking {
            if (alg.costGraph.objectives.size > 1)
                alg.population.asSequence()
                    .filter { it.iteration == alg.iteration }
                    .shuffled()
                    .slice(0 until (alg.population.size / 4))
                    .forEach { child ->
                        launch {
                            val firstCutIndex = Random.nextInt(alg.costGraph.objectives.indices)
                            val secondCutIndex = Random.nextInt(alg.costGraph.objectives.indices)
                                .let {
                                    if (it == firstCutIndex)
                                        (it + 1) % alg.costGraph.objectives.size
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
                            if (!child.checkFormat())
                                throw Error("Invalid specimen!")
                        }
                    }
        }
    },REVERSE_OR_RESET{
        override fun <S  : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>) = runBlocking {
            if(alg.iteration % 100 == 0)
                RESET(alg)
            else
                REVERSE(alg)
        }
    };

    abstract operator fun <S  : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<S>)
}
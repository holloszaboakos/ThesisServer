package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm
import kotlin.random.Random
import kotlin.random.nextInt

enum class EMutateChildren {
    SWAP {
        override fun invoke(alg: GeneticAlgorithm) {
            if (alg.objectives.size <= 1) {
                return
            }
            alg.population
                .filter { it.iteration == alg.iteration }
                .shuffled()
                .slice(0..alg.population.size / 4)
                .forEach { child ->
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
    },

    REVERSE {
        override fun invoke(alg: GeneticAlgorithm) {
            if (alg.objectives.size <= 1) {
                return
            }
            alg.population
                .filter { it.iteration == alg.iteration }
                .shuffled()
                .slice(0..(alg.population.size / 4))
                .forEach { child ->
                    val firstCutIndex = Random.nextInt(alg.objectives.indices)
                    val secondCutIndex = Random.nextInt(alg.objectives.indices)
                        .let {
                            if (it == firstCutIndex)
                                (it + 1) % alg.objectives.size
                            else
                                it
                        }

                    if(secondCutIndex>firstCutIndex) {
                        val reversed = child.values.slice(firstCutIndex..secondCutIndex).reversed()
                        for(geneIndex in firstCutIndex..secondCutIndex)
                            child[geneIndex] = reversed[geneIndex-firstCutIndex]
                    }
                    else{
                        val reversed = child.values.slice(secondCutIndex..firstCutIndex).reversed()
                        for(geneIndex in secondCutIndex..firstCutIndex)
                            child[geneIndex] = reversed[geneIndex-secondCutIndex]
                    }
                }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
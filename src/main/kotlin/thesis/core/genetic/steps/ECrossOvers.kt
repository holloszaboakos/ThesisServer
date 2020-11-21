package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm
import kotlin.random.Random
import kotlin.random.nextInt

enum class ECrossOvers {
    ORDERED {
        override fun invoke(alg: GeneticAlgorithm) {
            val children = alg.population.filter { !it.alive }
            val parent = alg.population.filter { it.alive }.shuffled()
            parent.forEachIndexed { index, primerParent ->
                val secunderParent =
                    if (index % 2 == 0)
                        parent[index + 1]
                    else
                        parent[index - 1]
                val child = children[index]
                val flags = Array(primerParent.values.size) { Random.nextBoolean() }
                primerParent.values.forEachIndexed { geneIndex, gene ->
                    child[geneIndex] =
                        if (flags[geneIndex])
                            gene
                        else
                            -1
                }
                flags.forEachIndexed { flagIndex, flag ->
                    if (!flag)
                        child[flagIndex] = secunderParent.values.first { !child.values.contains(it) }
                }
                primerParent.sliceLengthes.forEachIndexed { dividerIndex, divider ->
                    child.sliceLengthes[dividerIndex] = divider
                }
                child.alive = false
                child.iteration = alg.iteration
            }

        }
    },

    HALF {
        override fun invoke(alg: GeneticAlgorithm) {
            val children = alg.population.filter { !it.alive }
            val parent = alg.population.filter { it.alive }.shuffled()
            parent.forEachIndexed { index, primerParent ->

                val secunderParent = parent[index + 1 - 2 * (index % 2)]
                val child = children[index]

                val primerParentSlice = primerParent.values
                    .slice(
                        0..Random.nextInt(alg.objectives.indices)
                    )
                primerParentSlice.forEachIndexed { geneIndex, gene -> child[geneIndex] = gene }

                (primerParentSlice.size until alg.objectives.size)
                    .forEach { geneIndex->
                        child[geneIndex] = secunderParent.values.first { !child.values.contains(it) }
                    }
            }

        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
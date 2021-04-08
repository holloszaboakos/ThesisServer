package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm
import thesis.core.permutation.EPermutationFactory
import thesis.core.permutation.TwoPartRepresentation
import java.math.BigDecimal
import kotlin.random.Random
import kotlin.random.nextInt

enum class ECrossOvers {
    ORDERED {
        override fun invoke(alg: GeneticAlgorithm) {
            val children = alg.population.filter { !it.alive }
            val parent = alg.population.filter { it.alive }.shuffled()
            parent.forEachIndexed { index, primerParent ->
                val seconderParent =
                    if (index % 2 == 0)
                        parent[index + 1]
                    else
                        parent[index - 1]
                alg.crossoverOperator(Pair(primerParent,seconderParent),children[index])
            }

        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
package thesis.logic.genetic.steps

import thesis.logic.genetic.DGeneticAlgorithm
import thesis.logic.permutation.IPermutation

enum class EBoost {
    OPT2 {
        override fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>) {
            val best = alg.population[0]
            var bestCost = best.cost
            var improve = true
            var tempGene: Int
            while (improve) {
                improve = false
                (0 until best.size - 1).forEach { firstIndex ->
                    (firstIndex until best.size).forEach { secondIndex ->
                        tempGene = best[firstIndex]
                        best[firstIndex] = best[secondIndex]
                        best[secondIndex] = tempGene
                        alg.cost(best)
                        if (best.cost < bestCost) {
                            improve = true
                            bestCost = best.cost
                        } else {
                            tempGene = best[firstIndex]
                            best[firstIndex] = best[secondIndex]
                            best[secondIndex] = tempGene
                            best.cost = bestCost
                        }
                    }
                }
            }
        }
    },

    NO_BOOST {
        override fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>) {}

    };

    abstract operator fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>)
}
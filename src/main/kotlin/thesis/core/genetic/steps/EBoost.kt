package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm

enum class EBoost {
    OPT2 {
        override fun invoke(alg: GeneticAlgorithm) {
            val best = alg.population[0]
            var bestCost = best.cost
            var improve = true
            var tempGene: Int
            while (improve) {
                improve = false
                (best.values.indices).forEach { firstIndex ->
                    (best.values.indices).forEach { secondIndex ->
                        if (firstIndex != secondIndex) {
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
                            }
                        }
                    }
                }
            }
        }
    },

    NO_BOOST {
        override fun invoke(alg: GeneticAlgorithm) {}

    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
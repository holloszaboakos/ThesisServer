package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation

enum class EBoost {
    OPT2 {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {
            val best = alg.population[0]
            var bestCost = best.cost
            var improve = true
            var tempGene: Int
            while (improve) {
                improve = false
                for (firstIndex in (0 until best.size - 1).shuffled()) {
                    for (secondIndex in (firstIndex until best.size).shuffled()) {
                        tempGene = best[firstIndex]
                        best[firstIndex] = best[secondIndex]
                        best[secondIndex] = tempGene
                        alg.cost(best)
                        if (best.cost < bestCost) {
                            improve = true
                            println(best.cost)
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

    OPT2_STEP_ON_WORST {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {
            val worst = alg.population.last()
            val oldCost = worst.cost
            var tempGene: Int
            for (firstIndex in (0 until worst.size - 1).shuffled()) {
                for (secondIndex in (firstIndex until worst.size).shuffled()) {
                    tempGene = worst[firstIndex]
                    worst[firstIndex] = worst[secondIndex]
                    worst[secondIndex] = tempGene
                    alg.cost(worst)
                    if (worst.cost < oldCost) {
                        break
                    } else {
                        tempGene = worst[firstIndex]
                        worst[firstIndex] = worst[secondIndex]
                        worst[secondIndex] = tempGene
                        worst.cost = oldCost
                    }
                }
            }

        }
    },

    OPT2_ON_WORST {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {
            val worst = alg.population.last()
            var bestCost = worst.cost
            var improve = true
            var tempGene: Int
            while (improve) {
                improve = false
                for (firstIndex in (0 until worst.size - 1).shuffled()) {
                    for (secondIndex in (firstIndex until worst.size).shuffled()) {
                        tempGene = worst[firstIndex]
                        worst[firstIndex] = worst[secondIndex]
                        worst[secondIndex] = tempGene
                        alg.cost(worst)
                        if (worst.cost < bestCost) {
                            improve = true
                            bestCost = worst.cost
                        } else {
                            tempGene = worst[firstIndex]
                            worst[firstIndex] = worst[secondIndex]
                            worst[secondIndex] = tempGene
                            worst.cost = bestCost
                        }
                    }
                }
            }
        }
    },

    OPT2_STEP {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {
            val best = alg.population.first()
            if(best.iteration != alg.iteration)
                return
            val oldCost = best.cost
            var tempGene: Int
            for (firstIndex in (0 until best.size - 1).asSequence().shuffled()) {
                for (secondIndex in (firstIndex until best.size).asSequence().shuffled()) {
                    tempGene = best[firstIndex]
                    best[firstIndex] = best[secondIndex]
                    best[secondIndex] = tempGene
                    alg.cost(best)
                    if (best.cost < oldCost) {
                        best.iteration = alg.iteration + 1
                        break
                    } else {
                        tempGene = best[firstIndex]
                        best[firstIndex] = best[secondIndex]
                        best[secondIndex] = tempGene
                        best.cost = oldCost
                    }
                }
            }

        }
    },

    NO_BOOST {
        override fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>) {}

    };

    abstract operator fun <P : IRepresentation> invoke(alg: DGeneticAlgorithm<P>)
}
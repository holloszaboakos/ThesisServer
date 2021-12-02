package hu.bme.thesis.logic.evolutionary.common

import hu.bme.thesis.logic.evolutionary.SEvolutionaryAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.*
import kotlin.random.Random

enum class EBoost {
    OPT2 {
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            val best = alg.population[0]
            var bestCost = best.cost
            var improve = true
            var tempGene: Int
            while (improve) {
                improve = false
                for (firstIndex in (0 until best.permutationSize - 1).shuffled()) {
                    for (secondIndex in (firstIndex + 1 until best.permutationSize).shuffled()) {
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
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            val worst = alg.population.last()
            val oldCost = worst.cost
            var tempGene: Int
            OUTER@ for (firstIndex in (0 until worst.permutationSize - 1).shuffled()) {
                for (secondIndex in (firstIndex + 1 until worst.permutationSize).shuffled()) {
                    tempGene = worst[firstIndex]
                    worst[firstIndex] = worst[secondIndex]
                    worst[secondIndex] = tempGene
                    alg.cost(worst)
                    if (worst.cost < oldCost) {
                        break@OUTER
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
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            val worst = alg.population.last()
            var bestCost = worst.cost
            var improve = true
            var tempGene: Int
            while (improve) {
                improve = false
                for (firstIndex in (0 until worst.permutationSize - 1).shuffled()) {
                    for (secondIndex in (firstIndex + 1 until worst.permutationSize).shuffled()) {
                        tempGene = worst[firstIndex]
                        worst[firstIndex] = worst[secondIndex]
                        worst[secondIndex] = tempGene
                        alg.cost(worst)
                        if (worst.cost < bestCost) {
                            println("best: $bestCost")
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

    OPT2_CYCLE {
        private var bestCost = Double.MAX_VALUE
        private var improve = false
        private var reset = true
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            val best = alg.population.first()
            if (!improve && best.cost == bestCost) {
                if (reset) {
                    //EMutateChildren.RESET(alg)
                    reset = false
                }
                return
            }
            reset = true
            println("BOOST")
            improve = false
            bestCost = best.cost
            var tempGene: Int
            for (firstIndex in (0 until best.permutationSize - 1).asSequence().shuffled()) {
                for (secondIndex in (firstIndex + 1 until best.permutationSize).asSequence().shuffled()) {
                    tempGene = best[firstIndex]
                    best[firstIndex] = best[secondIndex]
                    best[secondIndex] = tempGene
                    alg.cost(best)
                    if (best.cost < bestCost) {
                        bestCost = best.cost
                        improve = true
                        println("best: $bestCost")
                    } else {
                        tempGene = best[firstIndex]
                        best[firstIndex] = best[secondIndex]
                        best[secondIndex] = tempGene
                        best.cost = bestCost
                    }
                }
            }

        }
    },

    OPT2_STEP {
        private var bestCost = Double.MAX_VALUE
        private var improve = false
        private var reset = true
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {
            val best = alg.population.first()
            if (!improve && best.cost == bestCost) {
                if (reset) {
                    //EMutateChildren.RESET(alg)
                    reset = false
                }
                return
            }
            reset = true
            println("BOOST")
            improve = false
            bestCost = best.cost
            var tempGene: Int
            outer@ for (firstIndex in (0 until best.permutationSize - 1)) {
                for (secondIndex in (firstIndex + 1 until best.permutationSize)) {
                    tempGene = best[firstIndex]
                    best[firstIndex] = best[secondIndex]
                    best[secondIndex] = tempGene
                    alg.cost(best)
                    if (best.cost < bestCost) {
                        bestCost = best.cost
                        improve = true
                        break@outer
                    } else {
                        tempGene = best[firstIndex]
                        best[firstIndex] = best[secondIndex]
                        best[secondIndex] = tempGene
                        best.cost = bestCost
                    }
                }
            }

        }
    },

    OPT2_CYCLE_ON_BEST_AND_LUCKY {
        private fun <S : ISpecimenRepresentation> opt2Cycle(alg: SEvolutionaryAlgorithm<S>, specimen: S, index: Int) {
            var bestCost = specimen.cost
            var tempGene: Int
            for (firstIndex in 0 until specimen.permutationSize - 1) {
                for (secondIndex in firstIndex + 1 until specimen.permutationSize) {
                    tempGene = specimen[firstIndex]
                    specimen[firstIndex] = specimen[secondIndex]
                    specimen[secondIndex] = tempGene
                    alg.cost(specimen)
                    if (specimen.cost < bestCost) {
                        bestCost = specimen.cost
                    } else {
                        tempGene = specimen[firstIndex]
                        specimen[firstIndex] = specimen[secondIndex]
                        specimen[secondIndex] = tempGene
                        specimen.cost = bestCost
                    }
                }
                if (firstIndex % 100 == 0)
                    print(".$index")
            }
        }

        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) = runBlocking {
            coroutineScope {

                println("BOOST")
                launch(Dispatchers.Default) {
                    opt2Cycle(alg, alg.population.first(), 0)
                }
                println("BOOSTED")
                alg.population
                    .slice(1 until alg.population.size)
                    .forEachIndexed { index, it ->
                        if (Random.nextInt(10) == 0) {
                            launch(Dispatchers.Default) {
                                opt2Cycle(alg, it, index + 1)
                                println("BOOSTED")
                            }
                        }
                    }
            }

        }
    },

    NO_BOOST {
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {}

    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>)
}
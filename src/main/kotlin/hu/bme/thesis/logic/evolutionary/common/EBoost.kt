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
                for (firstIndex in (0 until best.permutationIndices.count() - 1).shuffled()) {
                    for (secondIndex in (firstIndex + 1 until best.permutationIndices.count()).shuffled()) {
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
            OUTER@ for (firstIndex in (0 until worst.permutationIndices.count() - 1).shuffled()) {
                for (secondIndex in (firstIndex + 1 until worst.permutationIndices.count()).shuffled()) {
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
                for (firstIndex in (0 until worst.permutationIndices.count() - 1).shuffled()) {
                    for (secondIndex in (firstIndex + 1 until worst.permutationIndices.count()).shuffled()) {
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
            for (firstIndex in (0 until best.permutationIndices.count() - 1).asSequence().shuffled()) {
                for (secondIndex in (firstIndex + 1 until best.permutationIndices.count()).asSequence().shuffled()) {
                    tempGene = best[firstIndex]
                    best[firstIndex] = best[secondIndex]
                    best[secondIndex] = tempGene
                    alg.cost(best)
                    if (best.cost < bestCost) {
                        bestCost = best.cost
                        improve = true
                        //println("best: $bestCost")
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
        private var order: IntArray? = null
        private var lastChecked = 0
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {

            val best = alg.population.first()
            if (!improve || best.cost != bestCost || order == null) {
                order = (0 until best.permutationIndices.count()).shuffled().toIntArray()
                lastChecked = 0
            }
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
            outer@ for (firstIndex in order!!.slice(lastChecked until best.permutationIndices.count() - 1)) {
                for (secondIndex in order!!.slice(firstIndex + 1 until best.permutationIndices.count())) {
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
                lastChecked++
            }

        }
    },

    OPT2_CYCLE_ON_BEST_AND_LUCKY {
        var positionPairs: MutableList<Pair<Int, Int>> = mutableListOf()
        var geneIndex: Int = 0

        private fun <S : ISpecimenRepresentation> opt2Cycle(alg: SEvolutionaryAlgorithm<S>, specimen: S) {
            var bestCost = specimen.cost
            var tempGene: Int
            positionPairs
                .slice(geneIndex until (geneIndex + specimen.permutationSize))
                .forEach { indexes ->
                    tempGene = specimen[indexes.first]
                    specimen[indexes.first] = specimen[indexes.second]
                    specimen[indexes.second] = tempGene
                    alg.cost(specimen)
                    if (specimen.cost < bestCost) {
                        bestCost = specimen.cost
                    } else {
                        tempGene = specimen[indexes.first]
                        specimen[indexes.first] = specimen[indexes.second]
                        specimen[indexes.second] = tempGene
                        specimen.cost = bestCost
                    }
                    print(".")
                }
            geneIndex += specimen.permutationSize
            println()
        }

        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) = runBlocking {
            if (positionPairs.isEmpty()) {
                val geneCount = alg.population.first().permutationSize
                println("LOAD DATA")
                for (firstIndex in 0 until geneCount - 1) {
                    for (secondIndex in firstIndex + 1 until geneCount) {
                        positionPairs.add(Pair(firstIndex, secondIndex))
                        print(".")
                    }
                    println()
                }
                println("SHUFFLE DATA")
                positionPairs.shuffle()
            }
            if (geneIndex == positionPairs.size) {
                println("SHUFFLE DATA")
                positionPairs.shuffle()
                geneIndex = 0
            }
            println("BOOST")
            opt2Cycle(alg, alg.population.first())
            println("BOOSTED")
            alg.population
                .slice(1 until alg.population.size)
                .forEachIndexed { _, it ->
                    if (Random.nextInt(10) == 0) {
                        opt2Cycle(alg, it)
                        println("BOOSTED")
                    }
                }

        }
    },

    NO_BOOST {
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) {}

    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>)
}
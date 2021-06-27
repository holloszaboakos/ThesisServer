package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.permutation.IPermutation
import kotlin.math.abs
import kotlin.random.Random

enum class ECrossOverOperator {
    PARTIALLY_MATCHED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val cut = arrayOf(Random.nextInt(parents.first.size), Random.nextInt(parents.first.size - 1))
            if (cut[0] == cut[1])
                cut[1]++
            cut.sort()
            val primerParent = parents.first
            val seconderParent = parents.second
            val mapping = mutableListOf<IntArray>()

            //clean child
            child.setEach { _, _ -> -1 }
            //copy parent middle to child
            //start mapping
            primerParent.slice(cut[0]..cut[1])
                .forEachIndexed { valueIndex, value ->
                    mapping.add(intArrayOf(value, seconderParent[cut[0] + valueIndex]))
                    child[cut[0] + valueIndex] = value
                }
            //fill missing places of child
            child.setEach { valueIndex, value ->
                if (value == -1) {
                    var newValue = seconderParent[valueIndex]
                    while (child.contains(newValue)) {
                        newValue = mapping.first { it[0] == newValue }[1]
                    }
                    newValue
                } else
                    value
            }
            child.iteration = alg.iteration
            
                
        }
    },
    ORDER {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = parents.toList()
            val cut = arrayOf(Random.nextInt(parentsL.size), Random.nextInt(parentsL.size - 1))
            if (cut[0] == cut[1])
                cut[1]++
            cut.sort()

            val primerParent = parents.first
            val seconderParent = parents.second

            //clean child
            child.setEach { _, _ -> -1 }
            //copy parent middle to child
            primerParent.slice(cut[0]..cut[1])
                .forEachIndexed { valueIndex, value ->
                    child[cut[0] + valueIndex] = value
                }
            //fill missing places of child
            (0 until child.size).forEach { valueIndex ->
                if (child[valueIndex] == -1) {
                    child[valueIndex] = seconderParent.first { !child.contains(it) }
                }
            }
            child.setEach { _, value ->
                if (value == -1) {
                    seconderParent.first { !child.contains(it) }
                } else
                    value
            }
            child.iteration = alg.iteration
            
                
        }

    },
    ORDER_BASED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val primerParent = parents.first
            val seconderParent = parents.second
            val selected = BooleanArray(child.size) { Random.nextBoolean() }

            //clean child
            //copy parent middle to child
            child.setEach { valueIndex, _ ->
                if (selected[valueIndex])
                    primerParent[valueIndex]
                else
                    -1
            }

            //fill missing places of child
            child.setEach { _, value ->
                if (value == -1) {
                    seconderParent.first { !child.contains(it) }
                } else
                    value
            }
            child.iteration = alg.iteration
            
                

        }
    },
    POSITION_BASED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val primerParent = parents.first
            val seconderParent = parents.second
            val selected1 = BooleanArray(child.size) { Random.nextBoolean() }
            val selected2 = BooleanArray(child.size) { Random.nextBoolean() }

            //clean child
            //copy parent middle to child
            child.setEach { valueIndex, _ ->
                if (selected1[valueIndex] && selected2[valueIndex])
                    primerParent[valueIndex]
                else
                    -1
            }

            //fill missing places of child
            child.setEach { _, value ->
                if (value == -1) {
                    seconderParent.first { !child.contains(it) }
                } else
                    value
            }
            child.iteration = alg.iteration
            
                
        }

    },
    CYCLE_BASED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val primerParent = parents.first
            val seconderParent = parents.second

            //clean child
            //copy parent middle to child
            child.setEach { _, _ -> -1 }

            child[0] = primerParent[0]
            //fill missing places of child
            var actualIndex = seconderParent.indexOf(child[0])
            while (actualIndex != primerParent[0]) {
                child[actualIndex] = primerParent[actualIndex]
                actualIndex = seconderParent.indexOf(actualIndex)
            }

            //fill missing places of child
            child.setEach { _, value ->
                if (value == -1) {
                    seconderParent.first { !child.contains(it) }
                } else
                    value

            }
            child.iteration = alg.iteration
            
                
        }
    },
    HEURISTIC {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = parents.toList()
            child.setEach { _, _ -> -1 }
            child[0] = Random.nextInt(child.size)
            for (geneIndex in 1 until child.size) {
                val previous = child[geneIndex - 1]
                val neighbours = listOf(
                    parentsL[0][(parentsL[0].indexOf(previous) + child.size - 1) % child.size],
                    parentsL[0][(parentsL[0].indexOf(previous) + 1) % child.size],
                    parentsL[1][(parentsL[0].indexOf(previous) + child.size - 1) % child.size],
                    parentsL[1][(parentsL[0].indexOf(previous) + 1) % child.size]
                ).filter { !child.contains(it) }
                if (neighbours.isEmpty()) {
                    try {
                        child[geneIndex] = parentsL[0].map { it }.filter { !child.contains(it) }.random()
                    } catch (e: NoSuchElementException) {
                        println("LOL")
                    }
                    continue
                }
                val weights = Array(neighbours.size) {
                    try {
                        1.0f / alg.costGraph.edgesBetween[previous].values[
                                if (neighbours[it] > previous)
                                    neighbours[it] - 1
                                else neighbours[it]
                        ].length_Meter
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        throw e
                    }
                }
                val sum = weights.sum()
                val choice = Random.nextFloat() * sum
                var fill = 0.0f
                weights.forEachIndexed { weightIndex, value ->
                    fill += value
                    if (child[geneIndex] == -1 && fill >= choice)
                        child[geneIndex] = neighbours[weightIndex]
                }
            }
            child.iteration = alg.iteration
            
                
        }
    },
    GENETIC_EDGE_RECOMBINATION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = parents.toList()
            val table = Array(parents.first.size) { index ->
                val values = mutableSetOf<Int>()
                parentsL.forEach { parent ->
                    if (parent.indexOf(index) != 0)
                        values += parent[parent.indexOf(index) - 1]
                    if (parent.indexOf(index) != parent.size - 1)
                        values += parent[parent.indexOf(index) + 1]
                }
                values
            }
            child.setEach { _, _ -> -1 }
            for (geneIndex in 0 until child.size) {

                val minimalLineLength = if (table.map { it.size }.any { it != 0 }) {
                    table.map { it.size }.filter { it != 0 }.toSet().minOf { it }
                } else
                    0
                if (minimalLineLength != 0) {
                    val chosenLine = table.filter { it.size == minimalLineLength }.random()
                    child[geneIndex] = table.indexOf(chosenLine)
                    table[child[geneIndex]].clear()
                    table.forEach {
                        it.remove(child[geneIndex])
                    }
                } else {
                    child[geneIndex] = (0 until child.size).first { !child.contains(it) }
                }
            }
            child.iteration = alg.iteration
            
        }
    },
    SORTED_MATCH {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsInverse = listOf(
                Array(parents.first.size) {
                    parents.first.indexOf(it)
                },
                Array(parents.second.size) {
                    parents.second.indexOf(it)
                }
            )
            var longestSliceSize = 0
            var foundSlices = listOf<IntArray>()
            for (firstValue in 0 until parents.first.size - 1) {
                for (secondValue in firstValue until parents.first.size) {
                    if (
                        parentsInverse[0][firstValue] - parentsInverse[0][secondValue]
                        ==
                        parentsInverse[1][firstValue] - parentsInverse[1][secondValue]
                        && abs(parentsInverse[0][firstValue] - parentsInverse[0][secondValue]) > longestSliceSize
                    ) {
                        val firstIndices =
                            arrayOf(parentsInverse[0][firstValue], parentsInverse[0][secondValue]).sorted()
                        val secondIndices =
                            arrayOf(parentsInverse[1][firstValue], parentsInverse[1][secondValue]).sorted()
                        val slices = listOf(
                            parents.first.slice(firstIndices[0]..firstIndices[1]),
                            parents.second.slice(secondIndices[0]..secondIndices[1])
                        )
                        if (slices[0].all { slices[1].contains(it) }) {
                            longestSliceSize = abs(parentsInverse[0][firstValue] - parentsInverse[0][secondValue])
                            foundSlices = slices.map { it.toIntArray() }
                        }
                    }
                }
            }
            if (foundSlices.isNotEmpty()) {
                val cheaperIndex = LongArray(2) { sliceIndex ->
                    (1 until foundSlices[sliceIndex].size).sumOf { geneIndex ->
                        alg.costGraph
                            .edgesBetween[foundSlices[sliceIndex][geneIndex - 1]]
                            .values[
                                if (foundSlices[sliceIndex][geneIndex] > foundSlices[sliceIndex][geneIndex - 1])
                                    foundSlices[sliceIndex][geneIndex] - 1
                                else
                                    foundSlices[sliceIndex][geneIndex]
                        ]
                            .length_Meter
                    }
                }.let { costs -> costs.indexOf(costs.minOf { it }) }
                val indices = Array(2) { index ->
                    parentsInverse[index][foundSlices[index].first()]..
                            parentsInverse[index][foundSlices[index].last()]
                }
                (0 until indices[0].first).forEach { geneIndex ->
                    child[geneIndex] = parents.toList()[0][geneIndex]
                }
                indices[0].forEach { geneIndex ->
                    child[geneIndex] = foundSlices[cheaperIndex][geneIndex - indices[0].first]
                }
                (indices[0].last + 1 until parents.first.size).forEach { geneIndex ->
                    child[geneIndex] = parents.toList()[0][geneIndex]
                }
            }
            child.iteration = alg.iteration
            
                
        }
    },
    MAXIMAL_PRESERVATION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val size = child.size / 4 + Random.nextInt(child.size / 4)
            val start = Random.nextInt(child.size - size)
            child.setEach { index, _ ->
                if (index in start until (start + size))
                    parents.first[index]
                else
                    -1
            }
            child.setEach { _, value ->
                if (value == -1)
                    parents.first.first { !child.contains(it) }
                else value
            }
            child.iteration = alg.iteration
            
                
        }
    },
    VOTING_RECOMBINATION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {

            child.setEach { index, _ ->
                if (parents.first[index] == parents.second[index])
                    parents.first[index]
                else -1
            }
            val randomPermutation = (0 until child.size).shuffled()
            child.setEach { _, value ->
                if (value == -1)
                    randomPermutation.first { !child.contains(it) }
                else value
            }
            child.iteration = alg.iteration
            
                
        }
    },
    ALTERNATING_POSITION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = listOf(parents.first, parents.second)
            child.setEach { _, _ -> -1 }
            (0 until child.size).forEach { geneIndex ->
                parentsL.forEach { parent ->
                    if (!child.contains(parent[geneIndex]))
                        child[child.indexOf(-1)] = parent[geneIndex]
                }
            }
            child.iteration = alg.iteration
            
                
        }
    },
    ALTERNATING_EDGE {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsNeighbouring = listOf(
                Array(parents.first.size) { index ->
                    parents.first[(parents.first.indexOf(index) + 1) % parents.first.size]
                },
                Array(parents.second.size) { index ->
                    parents.second[(parents.second.indexOf(index) + 1) % parents.second.size]
                }
            )
            child.setEach { _, _ -> -1 }
            child[0] = (0 until parents.first.size).random()
            (1 until child.size).forEach { nextGeneIndex ->
                val parent = parentsNeighbouring[nextGeneIndex % 2]
                child[nextGeneIndex] =
                    if (!child.contains(parent[child[nextGeneIndex - 1]]))
                        parent[child[nextGeneIndex - 1]]
                    else
                        (0 until child.size).filter { !child.contains(it) }.random()
            }
            child.iteration = alg.iteration
            
                

        }
    },
    SUB_TOUR_CHUNKS {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsNeighbouring = listOf(
                Array(parents.first.size) { index ->
                    parents.first[(parents.first.indexOf(index) + 1) % parents.first.size]
                },
                Array(parents.second.size) { index ->
                    parents.second[(parents.second.indexOf(index) + 1) % parents.second.size]
                }
            )
            var size = Random.nextInt(child.size / 2) + 1
            var parentIndex = 0

            child.setEach { _, _ -> -1 }

            child.setEach { nextGeneIndex, _ ->
                if (nextGeneIndex == 0) {
                    parents.first[0]
                } else {
                    val parent = parentsNeighbouring[parentIndex]
                    size--
                    if (size == 0) {
                        size = Random.nextInt(nextGeneIndex, child.size)
                        parentIndex = (parentIndex + 1) % 2
                    }
                    if (!child.contains(parent[child[nextGeneIndex - 1]]))
                        parent[child[nextGeneIndex - 1]]
                    else
                        (0 until child.size).filter { !child.contains(it) }.random()
                }
            }
            child.iteration = alg.iteration
            
                
        }
    },
    DISTANCE_PRESERVING {
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            child.setEach { index, _ ->
                if (parents.first[index] == parents.second[index])
                    parents.first[index]
                else
                    -1
            }
            child.setEach { index, value ->
                if (value == -1)
                    parents.second[parents.first.indexOf(parents.second[index])]
                else
                    value
            }
            child.iteration = alg.iteration
            
                
        }
    },

    /*TWO_PART_CHROMOSOME {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ){
            TODO("Not yet implemented")
        }
    },*/
    STATISTICAL_PROBABILITY {
        val operators = mutableMapOf<ECrossOverOperator, Float>()
        override fun <P : IPermutation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            if (operators.isEmpty()) {
                operators += values().filter { it != this }.map { Pair(it, 0.5f) }
            }
            val sum = operators.values.toFloatArray().sum()
            val choice = Random.nextDouble()
            val size = child.size
            var fil = 0.0f
            var found = false
            operators.forEach { (type, weight) ->
                fil += weight / sum
                if (!found && fil >= choice) {
                    found = true
                    type(parents, child, alg)
                    alg.cost(child)
                    if (parents.first.cost > child.cost)
                        operators[type] = (operators.getOrDefault(type, 0.5f) * size + 1) / size
                    if (parents.second.cost > child.cost)
                        operators[type] = (operators.getOrDefault(type, 0.5f) * size + 1) / size
                }
            }
            child.iteration = alg.iteration
            
        }
    };

    abstract operator fun <P : IPermutation> invoke(
        parents: Pair<P, P>,
        child: P,
        alg: DGeneticAlgorithm<P>
    )
}
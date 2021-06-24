package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.permutation.IPermutation
import kotlin.math.abs
import kotlin.random.Random

enum class ECrossOverOperator {
    PARTIALLY_MATCHED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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

        }
    },
    ORDER {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
                if(child[valueIndex] == -1){
                    child[valueIndex] = seconderParent.first { !child.contains(it) }
                }
            }
            child.setEach { _, value ->
                if (value == -1) {
                    seconderParent.first { !child.contains(it) }
                } else
                    value
            }
        }

    },
    ORDER_BASED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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

        }
    },
    POSITION_BASED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
        }

    },
    CYCLE_BASED {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
            while (actualIndex != 0) {
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
        }
    },
    HEURISTIC {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = parents.toList()
            child.setEach { _, _ -> -1 }
            child[0] = Random.nextInt(child.size)
            for (geneIndex in 1 until child.size) {
                val previous = child[geneIndex - 1]
                val neighbours = listOf(
                    parentsL[0][(parentsL[0].indexOf(previous) - 1) % child.size],
                    parentsL[0][(parentsL[0].indexOf(previous) + 1) % child.size],
                    parentsL[1][(parentsL[0].indexOf(previous) - 1) % child.size],
                    parentsL[1][(parentsL[0].indexOf(previous) + 1) % child.size]
                ).filter { !child.contains(it) }
                if (neighbours.isEmpty()) {
                    child[geneIndex] = parentsL[0].map { it }.filter { !child.contains(it) }.random()
                    continue
                }
                val weights = Array(neighbours.size) {
                    1.0f / alg.costGraph.edgesBetween[previous].values[neighbours[it]].length_Meter.toLong()
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
        }
    },
    GENETIC_EDGE_RECOMBINATION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
            for (geneIndex in 0 until child.size) {
                val minimalLineLength = table.map { it.size }.toSet().minOf { it }
                val chosenLine = table.filter { it.size == minimalLineLength }.random()
                child[geneIndex] = table.indexOf(chosenLine)
                table[child[geneIndex]].clear()
                table.forEach {
                    it.remove(child[geneIndex])
                }
            }
        }
    },
    SORTED_MATCH {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ){
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
                    (1 until foundSlices[sliceIndex].size).map { geneIndex ->
                        alg.costGraph
                            .edgesBetween[foundSlices[sliceIndex][geneIndex] - 1]
                            .values[foundSlices[sliceIndex][geneIndex]]
                            .length_Meter.toLong()
                    }.sum()
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
        }
    },
    MAXIMAL_PRESERVATION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val size = child.size / 4 + Random.nextInt(child.size / 4)
            val start = Random.nextInt(child.size - size)
            child.setEach { index, _ ->
                if (index < size)
                    parents.first[index + start]
                else
                    parents.second.first { !child.contains(it) }
            }
        }
    },
    VOTING_RECOMBINATION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
        }
    },
    ALTERNATING_POSITION {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
        }
    },
    ALTERNATING_EDGE {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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

            val parentsNL = listOf(parentsNeighbouring[0], parentsNeighbouring[1])
            child.setEach { _, _ -> -1 }
            child[0] = parents.first[0]
            (0 until child.size).forEach { geneIndex ->
                parentsNL.forEach { parentN ->
                    if (!child.contains(parentN[child.map { it }.last { it != -1 }]))
                        child[child.map { it }.indexOfFirst { it == -1 }] =
                            parentsNL[geneIndex % 2][child.map { it }.last { it != -1 }]
                }
            }
        }
    },
    SUB_TOUR_CHUNKS {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
            val parentsNL = listOf(parentsNeighbouring[0], parentsNeighbouring[1])
            var size = Random.nextInt(child.size) + 1
            child.setEach { index, _ ->
                when {
                    index == 0 -> Random.nextInt(child.size)
                    index < size -> parentsNL[0][child[index - 1]]
                    else -> -1
                }
            }
            var geneIndex = child.first { it == -1 }
            var parentIndex = 1
            var sliceSize = 0
            while (geneIndex < child.size) {
                if (sliceSize == 0) {
                    size = Random.nextInt(child.size - geneIndex)
                    parentIndex = (parentIndex + 1) % 2
                    if (!child.contains(parentsNL[parentIndex][geneIndex - 1]))
                        child[geneIndex] = parentsNL[parentIndex][geneIndex - 1]
                    else
                        child[geneIndex] = (0 until child.size).filter { !child.contains(it) }.random()
                }
                sliceSize = (sliceSize + 1) % size
                geneIndex++
            }
        }
    },
    DISTANCE_PRESERVING {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
        }
    },
    TWO_PART_CHROMOSOME {
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ){
            TODO("Not yet implemented")
        }
    },
    STATISTICAL_PROBABILITY {
        val operators = mutableMapOf<ECrossOverOperator, Float>()
        override fun <P : IPermutation> invoke(
            parents: Pair<P,P>,
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
        }
    };

    abstract operator fun <P : IPermutation> invoke(
        parents: Pair<P,P>,
        child: P,
        alg: DGeneticAlgorithm<P>
    )
}
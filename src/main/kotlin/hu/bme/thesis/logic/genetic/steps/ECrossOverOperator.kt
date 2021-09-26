package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.IRepresentation
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

enum class ECrossOverOperator {
    PARTIALLY_MATCHED {
        override fun <P : IRepresentation> invoke(
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
            val secunderCopy = MutableList(seconderParent.size) { seconderParent[it] }
            val secunderInverz = Array(seconderParent.size) { 0 }
            secunderCopy.forEachIndexed { index, value ->
                secunderInverz[value] = index
            }

            //copy parent middle to child
            //start mapping
            child.setEach { index, _ ->
                if (index in cut[0]..cut[1])
                    child.size
                else {
                    secunderCopy[secunderInverz[primerParent[index]]] = child.size
                    primerParent[index]
                }
            }
            secunderCopy.removeIf { it == child.size }
            //fill empty positions
            secunderCopy.forEachIndexed { index, value ->
                child[cut[0] + index] = value
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")
        }
    },

    ORDER {
        override fun <P : IRepresentation> invoke(
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
            val seconderCopy = MutableList(child.size) { seconderParent[it] }
            val seconderInverz = Array(child.size) { 0 }
            seconderCopy.forEachIndexed { index, value ->
                seconderInverz[value] = index
            }

            //clean child
            //copy parent middle to child
            child.setEach { index, _ ->
                if (index in cut[0]..cut[1]) {
                    seconderCopy[seconderInverz[primerParent[index]]] = child.size
                    primerParent[index]
                } else
                    child.size
            }
            seconderCopy.removeIf { it == child.size }
            //fill missing places of child
            var counter = -1
            child.setEach { _, value ->
                if (value == child.size) {
                    counter++
                    seconderCopy[counter]
                } else
                    value
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true
            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }

    },

    ORDER_BASED {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val primerParent = parents.first
            val seconderParent = parents.second
            val seconderCopy = MutableList(child.size) { seconderParent[it] }
            val seconderInverz = Array(child.size) { 0 }
            seconderCopy.forEachIndexed { index, value ->
                seconderInverz[value] = index
            }

            //clean child
            //copy parent middle to child
            child.setEach { valueIndex, _ ->
                if (Random.nextBoolean()) {
                    seconderCopy[seconderInverz[primerParent[valueIndex]]] = child.size
                    primerParent[valueIndex]
                } else
                    child.size
            }

            seconderCopy.removeIf { it == child.size }

            var counter = -1
            //fill missing places of child
            child.setEach { _, value ->
                if (value == child.size) {
                    counter++
                    seconderCopy[counter]
                } else
                    value
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true

            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")


        }
    },

    POSITION_BASED {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val primerParent = parents.first
            val seconderParent = parents.second
            val seconderCopy = MutableList(child.size) { seconderParent[it] }
            val seconderInverz = Array(child.size) { 0 }
            seconderCopy.forEachIndexed { index, value ->
                seconderInverz[value] = index
            }
            val selected = BooleanArray(child.size) { Random.nextBoolean() && Random.nextBoolean() }

            //clean child
            //copy parent middle to child
            child.setEach { valueIndex, _ ->
                if (selected[valueIndex]) {
                    seconderCopy[seconderInverz[primerParent[valueIndex]]] = child.size
                    primerParent[valueIndex]
                } else
                    child.size
            }
            seconderCopy.removeIf { it == child.size }

            //fill missing places of child
            var counter = -1
            child.setEach { _, value ->
                if (value == child.size) {
                    counter++
                    seconderCopy[counter]
                } else
                    value
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }

    },

    CYCLE {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val primerParent = parents.first
            val seconderCopy = MutableList(parents.second.size) { parents.second[it] }
            val seconderInverz = IntArray(seconderCopy.size) { 0 }
            seconderCopy.forEachIndexed { index, value ->
                seconderInverz[value] = index
            }

            //clean child
            //copy parent middle to child
            child.setEach { _, _ -> child.size }

            child[0] = primerParent[0]
            var actualIndex = seconderInverz[child[0]]
            seconderCopy[actualIndex] = child.size
            //fill missing places of child
            if (actualIndex != 0)
                while (actualIndex != 0) {
                    child[actualIndex] = primerParent[actualIndex]
                    actualIndex = seconderInverz[primerParent[actualIndex]]
                    seconderCopy[actualIndex] = child.size
                }
            seconderCopy.removeIf { it == child.size }

            //fill missing places of child
            var counter = -1
            child.setEach { _, value ->
                if (value == child.size) {
                    counter++
                    seconderCopy[counter]
                } else
                    value

            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }
    },

    HEURISTIC {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = parents.toList()
            val parentsInverse = Array(2) {
                IntArray(child.size) { 0 }
            }
            parentsL.forEachIndexed { parentIndex, parent ->
                parent.forEachIndexed { index, value ->
                    parentsInverse[parentIndex][value] = index
                }
            }
            val randomPermutation = List(child.size) { it }.shuffled().toIntArray()
            var lastIndex = 0
            val childContains = BooleanArray(child.size) { false }
            child.setEach { _, _ -> child.size }
            child[0] = Random.nextInt(child.size)
            childContains[child[0]] = true
            for (geneIndex in 1 until child.size) {
                val previous = child[geneIndex - 1]
                val neighbours = listOf(
                    parentsL[0][(parentsInverse[0][previous] + child.size - 1) % child.size],
                    parentsL[0][(parentsInverse[0][previous] + 1) % child.size],
                    parentsL[1][(parentsInverse[1][previous] + child.size - 1) % child.size],
                    parentsL[1][(parentsInverse[1][previous] + 1) % child.size]
                ).filter { !childContains[it] }
                if (neighbours.isEmpty()) {
                    for (index in lastIndex until randomPermutation.size) {
                        if (!childContains[randomPermutation[index]]) {
                            child[geneIndex] = randomPermutation[index]
                            lastIndex = index + 1
                            break
                        }
                    }
                    childContains[child[geneIndex]] = true

                    continue
                } else {
                    val weights = Array(neighbours.size) {
                        if (previous < alg.costGraph.objectives.size && neighbours[it] < alg.costGraph.objectives.size)
                            1.0f / alg.costGraph.edgesBetween[previous].values[
                                    if (neighbours[it] > previous)
                                        neighbours[it] - 1
                                    else neighbours[it]
                            ].length_Meter
                        else if (previous < alg.costGraph.objectives.size) {
                            1.0f / alg.costGraph.edgesToCenter[previous].length_Meter
                        } else if (neighbours[it] < alg.costGraph.objectives.size) {
                            1.0f / alg.costGraph.edgesFromCenter[neighbours[it]].length_Meter
                        } else 1.0f
                    }
                    val sum = weights.sum()
                    var choice = Random.nextFloat() * sum
                    for (weightIndex in weights.indices) {
                        choice -= weights[weightIndex]
                        if (choice <= 0) {
                            child[geneIndex] = neighbours[weightIndex]
                            childContains[child[geneIndex]] = true
                            break
                        }
                    }
                }
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }
    },
    //TODO broken,
    /*
    GENETIC_EDGE_RECOMBINATION {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        )  {
            val parentsL = parents.toList()
            val parentsInverses = List(2) { IntArray(child.size) { 0 } }
            parentsL.forEachIndexed { index, parent ->
                parent.forEachIndexed { valueIndex, value ->
                    parentsInverses[index][value] = valueIndex
                }
            }
            val childContains = BooleanArray(child.size) { false }
            val table = Array(parents.first.size) { index ->
                val values = mutableSetOf<Int>()
                parentsL.forEachIndexed { parentIndex, parent ->
                    if (parentsInverses[parentIndex][index] != 0)
                        values += parent[parentsInverses[parentIndex][index] - 1]
                    if (parentsInverses[parentIndex][index] != parent.size - 1)
                        values += parent[parentsInverses[parentIndex][index] + 1]
                }
                values
            }
            child.setEach { _, _ -> child.size }
            for (geneIndex in 0 until child.size) {

                val minimalLineLength = if (table.map { it.size }.any { it != 0 }) {
                    table.map { it.size }.filter { it != 0 }.toSet().minOf { it }
                } else
                    0
                if (minimalLineLength != 0) {
                    child[geneIndex] = table.indices.filter { table[it].size == minimalLineLength }.random()
                    childContains[child[geneIndex]] = true
                    table[child[geneIndex]].clear()
                    table.forEach {
                        it.remove(child[geneIndex])
                    }
                } else {
                    child[geneIndex] = (0 until child.size).first { !childContains[it] }
                    childContains[child[geneIndex]] = true
                }
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")
        }
    },*/

    /*
    //TODO broken,
    SORTED_MATCH {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        )  {
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
                            foundSlices = slices.map { it.toList().toIntArray() }.toList()
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
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }
    },*/
    MAXIMAL_PRESERVATION {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val size = child.size / 4 + Random.nextInt(child.size / 4)
            val start = Random.nextInt(child.size - size)
            val seconderCopy = MutableList(parents.second.size) { parents.second[it] }
            val seconderInverz = Array(seconderCopy.size) { 0 }
            seconderCopy.forEachIndexed { index, value ->
                seconderInverz[value] = index
            }
            child.setEach { index, _ ->
                if (index < size) {
                    seconderCopy[seconderInverz[parents.first[index + start]]] = child.size
                    parents.first[index + start]
                } else
                    child.size
            }
            seconderCopy.removeIf { it == child.size }

            seconderCopy.forEachIndexed { index, value ->
                child[size + index] = value
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }
    },

    VOTING_RECOMBINATION {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val childContains = Array(child.size) { false }

            child.setEach { index, _ ->
                if (parents.first[index] == parents.second[index]) {
                    childContains[parents.first[index]] = true
                    parents.first[index]
                } else child.size
            }
            val randomPermutation = (0 until child.size).shuffled()
            child.setEach { _, value ->
                if (value == child.size) {
                    val newValue = randomPermutation.first { !childContains[it] }
                    childContains[newValue] = true
                    newValue
                } else value
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }
    },


    ALTERNATING_POSITION {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = listOf(parents.first, parents.second)
            val childContains = BooleanArray(child.size) { false }
            child.setEach { _, _ -> child.size }

            var counter = 0
            (0 until child.size).forEach { geneIndex ->
                parentsL.forEach { parent ->
                    if (!childContains[parent[geneIndex]]) {
                        child[counter] = parent[geneIndex]
                        childContains[child[counter]] = true
                        counter++
                    }
                }
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }
    },

    ALTERNATING_EDGE {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val childContains = Array(child.size) { false }
            val randomPermutation = (0 until child.size).shuffled()
            var lastIndex = 0
            val parentsL = listOf(parents.first, parents.second)
            val parentsInverze = Array(2) {
                Array(parentsL[it].size) { 0 }
            }
            parentsL.forEachIndexed { parentIndex, parent ->
                parent.forEachIndexed { index, value ->
                    parentsInverze[parentIndex][value] = index
                }
            }
            val parentsNeighbouring = List(2) { parentIndex ->
                Array(parents.first.size) { index ->
                    parentsL[parentIndex][(parentsInverze[parentIndex][index] + 1) % parentsL[parentIndex].size]
                }
            }
            child.setEach { _, _ -> child.size }
            child[0] = (0 until child.size).random()
            childContains[child[0]] = true
            (1 until child.size).forEach { geneIndex ->
                val parent = parentsNeighbouring[geneIndex % 2]

                if (!childContains[parent[child[geneIndex - 1]]])
                    child[geneIndex] = parent[child[geneIndex - 1]]
                else {
                    for (index in lastIndex until randomPermutation.size) {
                        if (!childContains[randomPermutation[index]]) {
                            child[geneIndex] = randomPermutation[index]
                            lastIndex = index + 1
                            break
                        }
                    }
                }
                childContains[child[geneIndex]] = true
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")


        }
    },

    SUB_TOUR_CHUNKS {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val parentsL = parents.toList()
            val parentsInverze = Array(2) {
                IntArray(child.size) { 0 }
            }
            parentsL.forEachIndexed { parentIndex, parent ->
                parent.forEachIndexed { index, value ->
                    parentsInverze[parentIndex][value] = index
                }
            }
            val parentsNeighbouring = List(2) { parentIndex ->
                Array(parentsL[parentIndex].size) { index ->
                    parentsL[parentIndex][(parentsInverze[parentIndex][index] + 1) % parentsL[parentIndex].size]
                }
            }
            var size = Random.nextInt(child.size / 2) + 1
            var parentIndex = 0
            val childContains = Array(child.size) { false }

            child.setEach { _, _ -> child.size }

            child.setEach { nextGeneIndex, _ ->
                if (nextGeneIndex == 0) {
                    childContains[parents.first[0]] = true
                    parents.first[0]
                } else {
                    val parent = parentsNeighbouring[parentIndex]
                    size--
                    if (size == 0) {
                        size = Random.nextInt(nextGeneIndex, child.size)
                        parentIndex = (parentIndex + 1) % 2
                    }
                    val result = if (!child.contains(parent[child[nextGeneIndex - 1]])) {
                        parent[child[nextGeneIndex - 1]]
                    } else {
                        (0 until child.size).filter { !childContains[it] }.random()
                    }
                    childContains[result] = true
                    result
                }
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

        }
    },
    DISTANCE_PRESERVING {
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ) {
            val primaryInverz = Array(parents.first.size) { 0 }
            parents.first.forEachIndexed { index, value ->
                primaryInverz[value] = index
            }
            child.setEach { index, _ ->
                if (parents.first[index] == parents.second[index])
                    parents.first[index]
                else
                    -1
            }
            child.setEach { index, value ->
                if (value == -1)
                    parents.second[primaryInverz[parents.second[index]]]
                else
                    value
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!checkIfPermutationIsValid(child, alg))
                throw Error("Invalid permutation!")

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
    //tegyünk bele fuzzy logikát vagy szimulált lehülést
    //pár iterációnként teljesen véletlent válasszunk
    //a mostani a méh kolónia algoritmus scout fázis menjen bele
    //abc: artificial bee colony
    //cinti
    STATISTICAL_RACE {
        var iteration = -1
        private var operator: ECrossOverOperator? = null
        private var statistics: OperatorStatistics? = null
        override fun <P : IRepresentation> invoke(
            parents: Pair<P, P>,
            child: P,
            alg: DGeneticAlgorithm<P>
        ): Unit {
            /*
            if (alg.iteration == 0 && iteration != -1){
                iteration = -1
            }
             */
            if (operators.isEmpty()) {
                operators += values()
                    .filter { it != this@STATISTICAL_RACE }
                    //.map { Pair(it, OperatorStatistics(1, 2, 0.5)) }
                    .map { Pair(it, OperatorStatistics(1.0, 2, Int.MAX_VALUE.toDouble())) }
            }

            if (iteration != alg.iteration) {
                iteration = alg.iteration
                statistics?.let { statistics ->
                    synchronized(statistics) {
                        statistics.run = (statistics.run + alg.population.size) * 9 / 10
                        //statistics.improvement = statistics.improvement * 9 / 10
                        statistics.success = statistics.success * 9 / 10
                        //statistics.successRatio = statistics.improvement / statistics.run.toDouble()
                        statistics.successRatio = statistics.success / statistics.run
                    }
                }
                val sumOfSuccessRatio = operators.values.sumOf { it.successRatio }
                val choice = Random.nextDouble()
                var fill = 0.0
                var found = false
                operators.forEach { (type, value) ->
                    fill += value.successRatio / sumOfSuccessRatio
                    if (!found && fill >= choice) {
                        found = true
                        operator = type
                        println(type)
                        statistics = value
                    }
                }
            }

            operator?.let { operator ->
                statistics?.let { statistics ->
                    operator.invoke(parents, child, alg)
                    alg.cost(child)
                    synchronized(statistics) {
                        if ((alg.best?.cost ?: 0.0) > child.cost)
                            statistics.success += alg.costGraph.objectives.size

                        if (parents.first.cost > child.cost)
                            statistics.success += (alg.population.size - parents.first.orderInPopulation) / alg.population.size.toDouble() *
                                    (parents.first.cost - child.cost) / parents.first.cost * 2
                        /*(alg.population.size - parents.first.orderInPopulation) / alg.population.size.toDouble() *
                                (parents.first.cost - child.cost) / parents.first.cost*
                                (alg.population.size - parents.first.orderInPopulation) / alg.population.size.toDouble() *
                                (parents.first.cost - child.cost) / parents.first.cost*/
                        //statistics.improvement += ( parents.first.cost - child.cost) / parents.first.cost * 2
                        if (parents.second.cost > child.cost)
                            statistics.success += (alg.population.size - parents.second.orderInPopulation) / alg.population.size.toDouble() *
                                    (parents.second.cost - child.cost) / parents.second.cost
                        /*(alg.population.size - parents.second.orderInPopulation) / alg.population.size.toDouble() *
                                (parents.second.cost - child.cost) / parents.second.cost *
                                (alg.population.size - parents.second.orderInPopulation) / alg.population.size.toDouble() *
                                (parents.second.cost - child.cost) / parents.second.cost*/
                        //statistics.improvement += (parents.second.cost - child.cost) / parents.second.cost
                    }
                }
            }

        }
    };

    data class OperatorStatistics(
        //var improvement: Double,
        var success: Double,
        var run: Int,
        var successRatio: Double
    )

    abstract operator fun <P : IRepresentation> invoke(
        parents: Pair<P, P>,
        child: P,
        alg: DGeneticAlgorithm<P>
    )

    fun <P : IRepresentation> checkIfPermutationIsValid(permutation: P, alg: DGeneticAlgorithm<P>): Boolean {
        val numbers = (0 until permutation.size).toList().toTypedArray()
        var result = true
        permutation.forEach {
            if (it !in numbers)
                result = false
            else
                numbers[it] = -1
        }
        return if (permutation.mapSlice { it.toList().size }.sum() != alg.objectives.size)
            false
        else result
    }

    val operators = mutableMapOf<ECrossOverOperator, OperatorStatistics>()
}
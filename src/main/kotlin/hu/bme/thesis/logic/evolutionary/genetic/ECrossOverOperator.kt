package hu.bme.thesis.logic.evolutionary.genetic

import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlin.math.pow
import kotlin.random.Random

enum class ECrossOverOperator {

    PARTIALLY_MATCHED {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val cut = arrayOf(
                Random.nextInt(parents.first.permutationSize),
                Random.nextInt(parents.first.permutationSize - 1)
            )
            if (cut[0] == cut[1])
                cut[1]++
            cut.sort()
            val primerParent = parents.first
            val seconderParent = parents.second
            val seconderCopy = seconderParent.copyOfPermutationBy(::MutableList) as MutableList
            val seconderInverse = seconderParent.inverseOfPermutation()

            //copy parent middle to child
            //start mapping
            child.setEach { index, _ ->
                if (index in cut[0]..cut[1])
                    child.permutationSize
                else {
                    seconderCopy[seconderInverse[primerParent[index]]] = child.permutationSize
                    primerParent[index]
                }
            }
            seconderCopy.removeIf { it == child.permutationSize }
            //fill empty positions
            seconderCopy.forEachIndexed { index, value ->
                child[cut[0] + index] = value
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")
        }
    },

    ORDER {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val parentsL = parents.toList()
            val cut = arrayOf(Random.nextInt(parentsL.size), Random.nextInt(parentsL.size - 1))
            if (cut[0] == cut[1])
                cut[1]++
            cut.sort()

            val primerParent = parents.first
            val seconderParent = parents.second
            val seconderCopy = seconderParent.copyOfPermutationBy(::MutableList) as MutableList
            val seconderInverse = seconderParent.inverseOfPermutation()

            //clean child
            //copy parent middle to child
            child.setEach { index, _ ->
                if (index in cut[0]..cut[1]) {
                    seconderCopy[seconderInverse[primerParent[index]]] = child.permutationSize
                    primerParent[index]
                } else
                    child.permutationSize
            }
            seconderCopy.removeIf { it == child.permutationSize }
            //fill missing places of child
            var counter = -1
            child.setEach { _, value ->
                if (value == child.permutationSize) {
                    counter++
                    seconderCopy[counter]
                } else
                    value
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true
            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }

    },

    ORDER_BASED {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val primerParent = parents.first
            val seconderParent = parents.second
            val seconderCopy = seconderParent.copyOfPermutationBy(::MutableList) as MutableList
            val seconderInverse = seconderParent.inverseOfPermutation()

            //clean child
            //copy parent middle to child
            child.setEach { valueIndex, _ ->
                if (Random.nextBoolean()) {
                    seconderCopy[seconderInverse[primerParent[valueIndex]]] = child.permutationSize
                    primerParent[valueIndex]
                } else
                    child.permutationSize
            }

            seconderCopy.removeIf { it == child.permutationSize }

            var counter = -1
            //fill missing places of child
            child.setEach { _, value ->
                if (value == child.permutationSize) {
                    counter++
                    seconderCopy[counter]
                } else
                    value
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true

            if (!child.checkFormat())
                throw Error("Invalid specimen!")


        }
    },

    POSITION_BASED {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val primerParent = parents.first
            val seconderParent = parents.second
            val seconderCopy = seconderParent.copyOfPermutationBy(::MutableList) as MutableList
            val seconderInverse = seconderParent.inverseOfPermutation()
            val selected = BooleanArray(child.permutationSize) { Random.nextBoolean() && Random.nextBoolean() }

            //clean child
            //copy parent middle to child
            child.setEach { valueIndex, _ ->
                if (selected[valueIndex]) {
                    seconderCopy[seconderInverse[primerParent[valueIndex]]] = child.permutationSize
                    primerParent[valueIndex]
                } else
                    child.permutationSize
            }
            seconderCopy.removeIf { it == child.permutationSize }

            //fill missing places of child
            var counter = -1
            child.setEach { _, value ->
                if (value == child.permutationSize) {
                    counter++
                    seconderCopy[counter]
                } else
                    value
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }

    },

    CYCLE {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val primerParent = parents.first
            val seconderCopy = parents.second.copyOfPermutationBy(::MutableList) as MutableList
            val seconderInverse = parents.second.inverseOfPermutation()

            //clean child
            //copy parent middle to child
            child.setEach { _, _ -> child.permutationSize }

            child[0] = primerParent[0]
            var actualIndex = seconderInverse[child[0]]
            seconderCopy[actualIndex] = child.permutationSize
            //fill missing places of child
            if (actualIndex != 0)
                while (actualIndex != 0) {
                    child[actualIndex] = primerParent[actualIndex]
                    actualIndex = seconderInverse[primerParent[actualIndex]]
                    seconderCopy[actualIndex] = child.permutationSize
                }
            seconderCopy.removeIf { it == child.permutationSize }

            //fill missing places of child
            var counter = -1
            child.setEach { _, value ->
                if (value == child.permutationSize) {
                    counter++
                    seconderCopy[counter]
                } else
                    value

            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },

    HEURISTIC {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val parentsL = parents.toList()
            val parentsInverse = Array(2) {
                parentsL[it].inverseOfPermutation()
            }
            val randomPermutation = IntArray(child.permutationSize) { it }
            randomPermutation.shuffle()
            var lastIndex = 0
            val childContains = BooleanArray(child.permutationSize) { false }
            child.setEach { _, _ -> child.permutationSize }
            child[0] = Random.nextInt(child.permutationSize)
            childContains[child[0]] = true
            for (geneIndex in 1 until child.permutationSize) {
                val previous = child[geneIndex - 1]
                val neighbours = listOf(
                    parentsL[0][(parentsInverse[0][previous] + child.permutationSize - 1) % child.permutationSize],
                    parentsL[0][(parentsInverse[0][previous] + 1) % child.permutationSize],
                    parentsL[1][(parentsInverse[1][previous] + child.permutationSize - 1) % child.permutationSize],
                    parentsL[1][(parentsInverse[1][previous] + 1) % child.permutationSize]
                ).filter { !childContains[it] }
                if (neighbours.isEmpty()) {
                    for (index in lastIndex until randomPermutation.size) {
                        if (!childContains[randomPermutation[index]]) {
                            child[geneIndex] = randomPermutation[index]
                            childContains[child[geneIndex]] = true
                            lastIndex = index + 1
                            break
                        }
                    }
                    if (child[geneIndex] == child.permutationSize)
                        println("Fail")
                    continue
                } else {
                    val weights = Array(neighbours.size) {
                        if (previous < alg.costGraph.objectives.size && neighbours[it] < alg.costGraph.objectives.size)
                            1.0 / alg.costGraph.edgesBetween[previous].values[
                                    if (neighbours[it] > previous)
                                        neighbours[it] - 1
                                    else neighbours[it]
                            ].length_Meter
                        else if (previous < alg.costGraph.objectives.size) {
                            1.0 / alg.costGraph.edgesToCenter[previous].length_Meter
                        } else if (neighbours[it] < alg.costGraph.objectives.size) {
                            1.0 / alg.costGraph.edgesFromCenter[neighbours[it]].length_Meter
                        } else 1.0
                    }
                    val sum = weights.sum()
                    var choice = Random.nextDouble(sum)
                    for (weightIndex in weights.indices) {
                        choice -= weights[weightIndex]
                        if (choice <= 0) {
                            child[geneIndex] = neighbours[weightIndex]
                            childContains[child[geneIndex]] = true
                            break
                        }
                    }
                    if (child[geneIndex] == child.permutationSize)
                        println("Fail")
                }
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },

    GENETIC_EDGE_RECOMBINATION {

        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val parentsL = parents.toList()
            val parentsInverses = Array(2) { parentsL[it].inverseOfPermutation() }

            child.setEach { _, _ -> child.permutationSize }
            val childContains = BooleanArray(child.permutationSize) { false }
            val randomPermutation = IntArray(child.permutationSize) { it }
            randomPermutation.shuffle()
            var lastIndex = 0

            //O(n2)
            val table = Array(parents.first.permutationSize) { valueIndex ->
                val neighbours = mutableSetOf<Int>()

                parentsL.forEachIndexed { parentIndex, parent ->
                    if (parentsInverses[parentIndex][valueIndex] != 0)
                        neighbours += parent[parentsInverses[parentIndex][valueIndex] - 1]
                    if (parentsInverses[parentIndex][valueIndex] != child.permutationSize - 1)
                        neighbours += parent[parentsInverses[parentIndex][valueIndex] + 1]
                }
                neighbours
            }

            val neighbourCounts = Array(child.permutationSize) { valueIndex ->
                table[valueIndex].size
            }

            child[0] = parents.first[0]
            childContains[child[0]] = true
            table[child[0]].forEach { neighbour ->
                table[neighbour].remove(child[0])
                neighbourCounts[neighbour]--
            }
            //O(n2)
            for (geneIndex in 1 until child.permutationSize) {
                val previousGene = child[geneIndex - 1]
                val neighborsOfPrevious = table[previousGene]
                if (neighborsOfPrevious.isNotEmpty()) {
                    val neighbourCountsOfNeighbours = neighborsOfPrevious.map { neighbourCounts[it] }
                    val minCount = neighbourCountsOfNeighbours.minOf { it }
                    child[geneIndex] = neighborsOfPrevious
                        .filterIndexed { index, _ ->
                            neighbourCountsOfNeighbours[index] == minCount
                        }.random()
                } else {
                    for (index in lastIndex until randomPermutation.size) {
                        if (!childContains[randomPermutation[index]]) {
                            child[geneIndex] = randomPermutation[index]
                            lastIndex = index + 1
                            break
                        }
                    }
                }
                if (child[geneIndex] == child.permutationSize)
                    println("FUCK")
                childContains[child[geneIndex]] = true
                table[child[geneIndex]].forEach { neighbour ->
                    table[neighbour].remove(child[geneIndex])
                    neighbourCounts[neighbour]--
                }
                neighborsOfPrevious.clear()
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")
        }
    },

    /*
    //TODO broken,
    SORTED_MATCH {
        override fun <S : IRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: DGeneticAlgorithm<S>
        )  {
            val parentsInverse = listOf(
                Array(parents.first.permutationSize) {
                    parents.first.indexOf(it)
                },
                Array(parents.second.permutationSize) {
                    parents.second.indexOf(it)
                }
            )
            var longestSliceSize = 0
            var foundSlices = listOf<IntArray>()
            for (firstValue in 0 until parents.first.permutationSize - 1) {
                for (secondValue in firstValue until parents.first.permutationSize) {
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
                (indices[0].last + 1 until parents.first.permutationSize).forEach { geneIndex ->
                    child[geneIndex] = parents.toList()[0][geneIndex]
                }
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },*/
    MAXIMAL_PRESERVATION {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val size = child.permutationSize / 4 + Random.nextInt(child.permutationSize / 4)
            val start = Random.nextInt(child.permutationSize - size)
            val seconderCopy = parents.second.copyOfPermutationBy(::MutableList) as MutableList
            val seconderInverse = parents.second.inverseOfPermutation()

            child.setEach { index, _ ->
                if (index < size) {
                    seconderCopy[seconderInverse[parents.first[index + start]]] = child.permutationSize
                    parents.first[index + start]
                } else
                    child.permutationSize
            }
            seconderCopy.removeIf { it == child.permutationSize }

            seconderCopy.forEachIndexed { index, value ->
                child[size + index] = value
            }

            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },

    VOTING_RECOMBINATION {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val childContains = Array(child.permutationSize) { false }
            val randomPermutation = IntArray(child.permutationSize) { it }
            randomPermutation.shuffle()
            var lastIndex = 0

            child.setEach { index, _ ->
                if (parents.first[index] == parents.second[index]) {
                    childContains[parents.first[index]] = true
                    parents.first[index]
                } else
                    child.permutationSize
            }

            child.setEach { _, value ->
                if (value == child.permutationSize) {
                    var actualValue = child.permutationSize
                    for (actualIndex in lastIndex until child.permutationSize) {
                        if (!childContains[randomPermutation[actualIndex]]) {
                            actualValue = randomPermutation[actualIndex]
                            childContains[actualValue] = true
                            lastIndex = actualIndex + 1
                            break
                        }
                    }
                    actualValue
                } else
                    value
            }


            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },


    ALTERNATING_POSITION {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val parentsL = listOf(parents.first, parents.second)
            val childContains = BooleanArray(child.permutationSize) { false }
            child.setEach { _, _ -> child.permutationSize }

            var counter = 0
            (0 until child.permutationSize).forEach { geneIndex ->
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


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },

    ALTERNATING_EDGE {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val childContains = Array(child.permutationSize) { false }
            val randomPermutation = IntArray(child.permutationSize) { it }
            randomPermutation.shuffle()
            var lastIndex = 0
            val parentsL = listOf(parents.first, parents.second)
            val parentsNeighbouring = List(2) { parentIndex ->
                parentsL[parentIndex].sequentialOfPermutation()
            }
            child.setEach { _, _ -> child.permutationSize }
            child[0] = (0 until child.permutationSize).random()
            childContains[child[0]] = true
            (1 until child.permutationSize).forEach { geneIndex ->
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


            if (!child.checkFormat())
                throw Error("Invalid specimen!")


        }
    },

    SUB_TOUR_CHUNKS {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val parentsL = parents.toList()
            val parentsNeighbouring = List(2) { parentIndex ->
                parentsL[parentIndex].sequentialOfPermutation()
            }
            val randomPermutation = IntArray(child.permutationSize) { it }
            randomPermutation.shuffle()
            var lastIndex = 0
            var size = Random.nextInt(child.permutationSize / 2) + 1
            var parentIndex = 0
            val childContains = Array(child.permutationSize) { false }

            child.setEach { _, _ -> child.permutationSize }

            child.setEach { nextGeneIndex, _ ->
                if (nextGeneIndex == 0) {
                    childContains[parents.first[0]] = true
                    parents.first[0]
                } else {
                    val parent = parentsNeighbouring[parentIndex]
                    size--
                    if (size == 0) {
                        size = Random.nextInt(nextGeneIndex, child.permutationSize)
                        parentIndex = (parentIndex + 1) % 2
                    }
                    val result = if (!child.contains(parent[child[nextGeneIndex - 1]])) {
                        parent[child[nextGeneIndex - 1]]
                    } else {
                        var actualValue = child.permutationSize
                        for (index in lastIndex until child.permutationSize) {
                            if (!childContains[randomPermutation[index]]) {
                                actualValue = randomPermutation[index]
                                lastIndex = index + 1
                                break
                            }
                        }
                        actualValue
                    }
                    childContains[result] = true
                    result
                }
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },

    DISTANCE_PRESERVING {
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            val primaryInverse = parents.first.inverseOfPermutation()
            child.setEach { index, _ ->
                if (parents.first[index] == parents.second[index])
                    parents.first[index]
                else
                    -1
            }
            child.setEach { index, value ->
                if (value == -1)
                    parents.second[primaryInverse[parents.second[index]]]
                else
                    value
            }
            child.iteration = alg.iteration
            child.costCalculated = false
            child.inUse = true


            if (!child.checkFormat())
                throw Error("Invalid specimen!")

        }
    },

    /*TWO_PART_CHROMOSOME {
        override fun <S : IPermutation> invoke(
            parents: Pair<S,S>,
            child: S,
            alg: DGeneticAlgorithm<S>
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
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ) {
            /*
            if (alg.iteration == 0 && iteration != -1){
                iteration = -1
            }
             */
            synchronized(operators) {
                if (operators.isEmpty()) {
                    operators += values()
                        .filter { it != this@STATISTICAL_RACE && it != STATISTICAL_RACE_WITH_LEADER }
                        //.map { Pair(it, OperatorStatistics(1, 2, 0.5)) }
                        .map { Pair(it, OperatorStatistics(0.0, 1, Int.MAX_VALUE.toDouble())) }
                }
            }

            var newIteration: Boolean
            synchronized(iteration) {
                newIteration = iteration / 5 != alg.iteration / 5
                if (newIteration)
                    iteration = alg.iteration
            }

            if (newIteration) {
                statistics?.let { statistics ->
                    synchronized(statistics) {
                        statistics.run = (statistics.run + alg.population.size) * 8 / 10
                        //statistics.improvement = statistics.improvement * 9 / 10
                        statistics.success = statistics.success * 8 / 10
                        //statistics.successRatio = statistics.improvement / statistics.run.toDouble()
                        statistics.successRatio = statistics.success / statistics.run
                    }
                }
                if (iteration < 10 * operators.size) {
                    operator = operators.keys.toList()[iteration % operators.size]
                    println(operator)
                    statistics = operators[operator]
                } else {
                    val sumOfSuccessRatio = operators.values.sumOf { it.successRatio.pow(2) }
                    val choice = Random.nextDouble()
                    var fill = 0.0
                    var found = false
                    operators.forEach { (type, value) ->
                        fill += value.successRatio.pow(2) / sumOfSuccessRatio
                        if (!found && fill >= choice) {
                            found = true
                            operator = type
                            println(type)
                            statistics = value
                        }
                    }
                }
            }

            operator?.let { operator ->
                statistics?.let { statistics ->
                    synchronized(operators) {
                        operator.invoke(parents, child, alg)
                    }
                    alg.cost(child)
                    /*    AuditWorkstation, ExpeditionArea*/
                    synchronized(statistics) {
                        /*if (parents.first.cost > child.cost && parents.second.cost > child.cost) {
                            statistics.success +=
                                (alg.population.size - parents.first.orderInPopulation).toDouble().pow(2) *
                                        (alg.population.size - parents.second.orderInPopulation).toDouble().pow(2)
                        }
                        else*/ if (parents.first.cost > child.cost) {
                        statistics.success += (alg.iteration - parents.first.iteration) / child.cost /
                                (parents.first.orderInPopulation + 1).toDouble().pow(2)

                    }
                        /*else*/ if (parents.second.cost > child.cost) {
                        statistics.success += (alg.iteration - parents.second.iteration) / child.cost /
                                (parents.second.orderInPopulation + 1).toDouble().pow(2)
                    }
                    }
                }
            }

        }
    },

    STATISTICAL_RACE_WITH_LEADER {
        var iteration = -1
        private var operator: ECrossOverOperator? = null
        private var statistics: OperatorStatistics? = null
        override fun <S : ISpecimenRepresentation> invoke(
            parents: Pair<S, S>,
            child: S,
            alg: GeneticAlgorithm<S>
        ): Unit {
            /*
            if (alg.iteration == 0 && iteration != -1){
                iteration = -1
            }
             */
            if (operators.isEmpty()) {
                operators += values()
                    .filter { it != STATISTICAL_RACE_WITH_LEADER && it != STATISTICAL_RACE }
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

                if (iteration < 10 * operators.size) {
                    operator = operators.keys.toList()[iteration % operators.size]
                    println(operator)
                    statistics = operators[operator]
                } else {
                    operator = operators.maxByOrNull { it.value.successRatio }?.key
                    println(operator)
                    statistics = operators[operator]
                }
            }

            operator?.let { operator ->
                statistics?.let { statistics ->
                    operator.invoke(parents, child, alg)
                    alg.cost(child)
                    synchronized(statistics) {
                        if (parents.first.cost > child.cost)
                            statistics.success += (alg.iteration - parents.first.iteration) *
                                    (alg.population.size - parents.first.orderInPopulation) *
                                    ((parents.first.cost - child.cost) / parents.first.cost).pow(2) * 1.5

                        if (parents.second.cost > child.cost)
                            statistics.success += (alg.iteration - parents.second.iteration) *
                                    (alg.population.size - parents.second.orderInPopulation) *
                                    ((parents.second.cost - child.cost) / parents.second.cost).pow(2)
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

    data class ValueWithNeighbours(
        val value: Int,
        val neighbours: MutableSet<Int>,
    )

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        parents: Pair<S, S>,
        child: S,
        alg: GeneticAlgorithm<S>
    )

    val operators = mutableMapOf<ECrossOverOperator, OperatorStatistics>()
}
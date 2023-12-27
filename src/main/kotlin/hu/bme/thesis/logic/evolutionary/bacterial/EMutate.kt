package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.utility.extention.nextSegmentStartPosition
import hu.bme.thesis.utility.extention.selectRandomPositions
import hu.bme.thesis.utility.extention.shuffled
import kotlin.math.exp
import kotlin.random.Random

enum class EMutate {
    ELITIST_RANDOM_CONTINUES_SEGMENT {
        private fun <S : ISpecimenRepresentation> mutationOperator(
            clone: S,
            selectedPositions: IntArray,
            selectedElements: IntArray
        ) {
            selectedPositions
                .shuffled()
                .forEachIndexed { readIndex, writeIndex ->
                    clone[writeIndex] = selectedElements[readIndex]
                }
        }

        private fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.mutateSpecimen(specimen: S) {
            repeat(cloneCycleCount) {
                val randomPosition =
                    Random.nextSegmentStartPosition(specimen.permutationIndices.count(), cloneSegmentLength)
                val selectedPositions = IntArray(cloneSegmentLength) { randomPosition + it }
                val selectedElements = IntArray(cloneSegmentLength) { specimen[randomPosition + it] }

                val clones = MutableList(cloneCount + 1) { subSolutionFactory.copy(specimen) }
                clones
                    .slice(1 until clones.size)
                    .forEach { clone ->
                        mutationOperator(clone, selectedPositions, selectedElements)
                    }

                calcCostOfEach(clones)

                specimen.setData(clones.first().getData())
                specimen.cost = clones.first().cost
            }
        }

        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>
        ) {
            alg.run {

                population
                    .forEach { specimen ->
                        mutateSpecimen(specimen)
                    }

            }
        }
    },
    ELITIST_RANDOM_SPREAD_SEGMENT {
        private fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.mutationOperator(
            clone: S,
            selectedPositions: IntArray,
            selectedElements: IntArray
        ) {
            val shuffler = (0 until cloneSegmentLength).shuffled()
            selectedPositions.forEachIndexed { index, position ->
                clone[position] = selectedElements[shuffler[index]]
            }
        }

        private fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.mutateSpecimen(specimen: S) {
            repeat(cloneCycleCount) {
                val selectedPositions = specimen.permutationIndices.selectRandomPositions(cloneSegmentLength)

                val selectedElements = selectedPositions
                    .map { specimen[it] }
                    .toIntArray()

                val clones = MutableList(cloneCount + 1) { subSolutionFactory.copy(specimen) }

                clones
                    .slice(1 until clones.size)
                    .forEach { clone ->
                        mutationOperator(clone, selectedPositions, selectedElements)
                    }

                calcCostOfEach(clones)

                specimen.setData(clones.first().getData())
                specimen.cost = clones.first().cost
            }

        }

        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>
        ) {
            alg.run {

                population
                    .forEach { specimen ->
                        mutateSpecimen(specimen)

                    }
            }
        }
    },
    ELITIST_TOUCH_ALL_CONTINUES_SEGMENT {
        private fun <S : ISpecimenRepresentation> mutationOperator(
            clone: S,
            selectedPositions: IntArray,
            selectedElements: IntArray
        ) {
            selectedPositions
                .shuffled()
                .forEachIndexed { readIndex, writeIndex ->
                    clone[writeIndex] = selectedElements[readIndex]
                }
        }

        private fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.mutateSpecimen(
            specimen: S,
            randomStartPosition: Int
        ) {
            repeat(cloneCycleCount) { cycleCount ->

                val segmentPosition = randomStartPosition + cycleCount * cloneSegmentLength
                val clones = MutableList(cloneCount + 1) { subSolutionFactory.copy(specimen) }
                val selectedPositions = IntArray(cloneSegmentLength) { segmentPosition + it }
                val selectedElements = IntArray(cloneSegmentLength) { specimen[segmentPosition + it] }

                cost(specimen)
                clones
                    .slice(1 until clones.size)
                    .forEach { clone ->
                        mutationOperator(
                            clone,
                            selectedPositions,
                            selectedElements
                        )
                    }
                clones[0] = specimen
                calcCostOfEach(clones)
                specimen.setData(clones.first().getData())
                specimen.cost = clones.first().cost
            }
        }

        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>
        ) {
            alg.run {

                val randomStartPosition =
                    Random.nextInt(cloneSegmentLength)

                population
                    .forEach { specimen ->
                        mutateSpecimen(specimen, randomStartPosition)
                    }
            }
        }
    },
    ELITIST_TOUCH_ALL_SPREAD_SEGMENT {

        private fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.mutationOperator(
            clone: S,
            selectedPositions: IntArray,
            selectedElements: IntArray
        ) {
            val shuffler = (0 until cloneSegmentLength).shuffled()
            selectedPositions.forEachIndexed { index, position ->
                clone[position] = selectedElements[shuffler[index]]
            }
        }

        private fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.mutateSpecimen(
            specimen: S,
            randomPermutation: IntArray
        ) {
            repeat(cloneCycleCount) { cycleCount ->
                val segmentStart = cycleCount * cloneSegmentLength
                val segmentEnd = (cycleCount + 1) * cloneSegmentLength
                val selectedPositions = randomPermutation
                    .slice(segmentStart until segmentEnd)
                    .sortedBy { it }
                    .toIntArray()
                val selectedElements = selectedPositions
                    .map { specimen[it] }
                    .toIntArray()

                val clones = MutableList(cloneCount + 1) { subSolutionFactory.copy(specimen) }
                clones
                    .slice(1 until clones.size)
                    .forEach { clone ->
                        mutationOperator(clone, selectedPositions, selectedElements)
                    }

                calcCostOfEach(clones)

                specimen.setData(clones.first().getData())
                specimen.cost = clones.first().cost
            }
        }

        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>
        ) {
            alg.run {
                val randomPermutation = IntArray(geneCount) { it }.apply { shuffle() }

                population
                    .forEach { specimen ->
                        mutateSpecimen(specimen, randomPermutation)
                    }
            }
        }
    },
    SIMULATED_ANNEALING_TOUCH_ALL_CONTINUES_SEGMENT {

        private fun simulatedAnnealingHeat(iteration: Int, divider: Int): Float {
            return 1 / (1 + exp(iteration.toFloat() / divider))
        }

        private fun <S : ISpecimenRepresentation> mutationOperator(
            clone: S,
            selectedPositions: IntArray,
            selectedElements: IntArray
        ) {
            selectedPositions
                .shuffled()
                .forEachIndexed { readIndex, writeIndex ->
                    clone[writeIndex] = selectedElements[readIndex]
                }
        }

        private fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.mutateSpecimen(
            specimen: S,
            randomStartPosition: Int
        ) {
            repeat(cloneCycleCount) { cycleCount ->

                val segmentPosition = randomStartPosition + cycleCount * cloneSegmentLength
                val clones = MutableList(cloneCount + 1) { subSolutionFactory.copy(specimen) }
                val selectedPositions = IntArray(cloneSegmentLength) { segmentPosition + it }
                val selectedElements = IntArray(cloneSegmentLength) { specimen[segmentPosition + it] }

                cost(specimen)
                clones
                    .slice(1 until clones.size)
                    .forEach { clone ->
                        mutationOperator(
                            clone,
                            selectedPositions,
                            selectedElements
                        )
                    }
                clones[0] = specimen
                calcCostOfEach(clones)
                if (Random.nextFloat() > simulatedAnnealingHeat(iteration, iterationLimit)) {
                    specimen.setData(clones.first().getData())
                    specimen.cost = clones.first().cost
                } else {
                    specimen.setData(clones[1].getData())
                    specimen.cost = clones[1].cost
                }
            }
        }

        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>
        ) {
            alg.run {
                println(simulatedAnnealingHeat(iteration, iterationLimit))

                val randomStartPosition =
                    Random.nextInt(cloneSegmentLength)

                population.slice(1 until (population.size - 1))
                    .forEach { specimen ->
                        mutateSpecimen(specimen, randomStartPosition)
                    }
            }
        }
    };

    protected fun <S : ISpecimenRepresentation> BacterialAlgorithm<S>.calcCostOfEach(clones: MutableList<S>) {
        clones
            .onEach { cost(it) }
            .sortBy { it.cost }
    }

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        alg: BacterialAlgorithm<S>
    )
}
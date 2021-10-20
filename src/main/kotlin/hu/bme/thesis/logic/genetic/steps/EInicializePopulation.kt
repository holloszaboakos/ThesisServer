package hu.bme.thesis.logic.genetic.steps

import kotlinx.coroutines.launch
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.specimen.DOnePartRepresentation
import hu.bme.thesis.logic.specimen.DTwoPartRepresentation
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.random.nextInt

enum class EInicializePopulation {
    MODULO_STEPPER {
        override fun <S  : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>) = runBlocking {
            alg.run {
                val sizeOfPermutation = costGraph.objectives.size + salesmen.size - 1
                val basePermutation = IntArray(sizeOfPermutation) { it }
                alg.population.forEachIndexed { instanceIndex, instance ->
                    val step = instanceIndex % (sizeOfPermutation - 1) + 1
                    if (step == 1) {
                        basePermutation.shuffle()
                    }

                    val newContains = BooleanArray(sizeOfPermutation){false}
                    val newPermutation = IntArray(sizeOfPermutation) { -1 }
                    var baseIndex = step
                    for (newIndex in 0 until sizeOfPermutation) {
                        if (newContains[basePermutation[baseIndex]])
                            baseIndex = (baseIndex + 1) % sizeOfPermutation
                        newPermutation[newIndex] = basePermutation[baseIndex]
                        newContains[basePermutation[baseIndex]] = true
                        baseIndex = (baseIndex + step) % sizeOfPermutation
                    }

                    val breakPoints = newPermutation
                        .mapIndexed { index, value ->
                            if (value < costGraph.objectives.size)
                                -1
                            else
                                index
                        }
                        .filter { it != -1 }
                        .toMutableList()

                    breakPoints.add(0, -1)
                    breakPoints.add(sizeOfPermutation)
                    var it = -1
                    instance.setData(sequence {
                        it++
                        newPermutation.slice((breakPoints[it] + 1) until breakPoints[it + 1])

                    })
                    instance.iteration = 0
                    instance.costCalculated = false
                    instance.inUse = true
                    instance.cost=-1.0
                }
            }
        }
    },
    RANDOM {
        override fun <S  : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>) = runBlocking {
            alg.population.forEach { permutation ->
                launch {
                    permutation.shuffle()
                    var length = 0
                    when (permutation) {
                        is DTwoPartRepresentation ->
                            permutation.forEachSliceIndexed { index, _ ->
                                if (index == permutation.sliceLengths.size - 1) {
                                    permutation.sliceLengths[index] = alg.costGraph.objectives.size - length

                                } else {
                                    permutation.sliceLengths[index] =
                                        Random.nextInt(0..(alg.costGraph.objectives.size - length))
                                    length += permutation.sliceLengths[index]
                                }
                            }
                        is DOnePartRepresentation -> {

                        }
                    }
                    permutation.iteration = 0
                    permutation.costCalculated = false
                    permutation.inUse = true
                }
            }
        }
    };

    abstract operator fun <S  : ISpecimenRepresentation> invoke(alg: DGeneticAlgorithm<S>)
}
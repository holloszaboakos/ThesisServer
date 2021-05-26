package thesis.logic.genetic.steps

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import thesis.logic.genetic.DGeneticAlgorithm
import thesis.logic.permutation.DTwoPartRepresentation
import kotlin.random.Random
import kotlin.random.nextInt

enum class EInicializePopulation {
    RANDOM {
        override suspend fun invoke(alg: DGeneticAlgorithm<*>) = coroutineScope{
            alg.population.forEach { permutation ->
                launch {
                    permutation.shuffle()
                    var length = 0
                    when (permutation) {
                        is DTwoPartRepresentation ->
                            permutation.forEachSliceIndexed { index, _ ->
                                if (index == permutation.sliceLengths.size - 1) {
                                    permutation.sliceLengths[index] = alg.objectives.size - length

                                } else {
                                    permutation.sliceLengths[index] =
                                        Random.nextInt(0..(alg.objectives.size - length))
                                    length += permutation.sliceLengths[index]
                                }
                            }
                    }
                    permutation.iteration = -1
                    permutation.alive = true
                }
            }
        }
    };

    abstract suspend operator fun invoke(alg: DGeneticAlgorithm<*>)
}
package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm
import thesis.core.permutation.EPermutationFactory
import thesis.core.permutation.TwoPartRepresentation
import kotlin.random.Random
import kotlin.random.nextInt

enum class EInicializePopulation {
    RANDOM {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.population.forEach { permutation ->
                permutation.shuffle()
                var length = 0
                when(alg.setup.permutationFactory){
                    EPermutationFactory.TWO_PART_REPRESENTATION ->
                        if( permutation is TwoPartRepresentation){
                            permutation.forEachSliceIndexed { index, _ ->
                                if (index == permutation.sliceLengths.size - 1) {
                                    permutation.sliceLengths[index] = alg.objectives.size - length

                                } else {
                                    permutation.sliceLengths[index] = Random.nextInt(0..(alg.objectives.size - length))
                                    length += permutation.sliceLengths[index]
                                }
                            }
                        }
                }
                permutation.iteration = -1
                permutation.alive = true
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
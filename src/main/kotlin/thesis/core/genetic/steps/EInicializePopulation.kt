package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm
import kotlin.random.Random
import kotlin.random.nextInt

enum class EInicializePopulation {
    RANDOM {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.population.forEach { permutation ->
                permutation.values.shuffle()
                var length = 0
                permutation.sliceLengthes.forEachIndexed { index, _ ->
                    if (index == permutation.sliceLengthes.size - 1) {
                        permutation.sliceLengthes[index] = alg.objectives.size - length

                    } else {
                        permutation.sliceLengthes[index] = Random.nextInt(0..(alg.objectives.size - length))
                        length += permutation.sliceLengthes[index]
                    }
                }
                permutation.iteration = -1
                permutation.alive = true
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
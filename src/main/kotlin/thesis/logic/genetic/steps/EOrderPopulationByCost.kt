package thesis.logic.genetic.steps

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import thesis.logic.genetic.DGeneticAlgorithm
import thesis.logic.permutation.IPermutation
import java.math.BigDecimal

enum class EOrderPopulationByCost {
    RECALC_ALL {
        override suspend fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>) = coroutineScope{
            val jobs = alg.population.filter { it.cost == BigDecimal(-1) }.map {
              launch {
                  alg.cost(it)
              }
            }
            jobs.forEach { it.join() }
            alg.population = alg.population.sortedBy { it.cost }
            alg.population.forEach { it.alive = false }
        }
    };

    abstract suspend operator fun <P : IPermutation<out IPermutation.IFactory>> invoke(alg: DGeneticAlgorithm<P>)
}
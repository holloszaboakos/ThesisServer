package thesis.core.genetic.steps

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import thesis.core.genetic.GeneticAlgorithm
import java.math.BigDecimal

enum class EOrderPopulationByCost {
    RECALC_ALL {
        override suspend fun invoke(alg: GeneticAlgorithm) = coroutineScope{
            val jobs = alg.population.filter { it.cost == BigDecimal(-1) }.map {
              launch {
                  alg.cost(it)
              }
            }
            jobs.forEach { it.join() }
            alg.population.sortBy { it.cost }
            alg.population.forEach { it.alive = false }
        }
    };

    abstract suspend operator fun invoke(alg: GeneticAlgorithm)
}
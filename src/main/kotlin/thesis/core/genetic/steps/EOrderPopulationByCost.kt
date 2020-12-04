package thesis.core.genetic.steps

import thesis.core.genetic.GeneticAlgorithm
import java.math.BigDecimal

enum class EOrderPopulationByCost {
    RECALC_ALL {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.population.filter { it.cost== BigDecimal(-1) }.forEach { alg.cost(it) }
            alg.population.sortBy { it.cost }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
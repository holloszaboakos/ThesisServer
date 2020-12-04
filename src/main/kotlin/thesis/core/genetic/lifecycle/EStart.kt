package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm

enum class EStart {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            when (alg.state) {
                GeneticAlgorithm.State.CREATED -> {
                    alg.initialize()
                    alg.orderByCost()
                    alg.boost()
                }
                else -> {}
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
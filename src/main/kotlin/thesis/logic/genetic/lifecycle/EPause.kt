package thesis.logic.genetic.lifecycle

import thesis.logic.genetic.DGeneticAlgorithm

enum class EPause {
    STANDARD {
        override fun invoke(alg: DGeneticAlgorithm<*>) {
            when (alg.state) {
                DGeneticAlgorithm.State.RESUMED -> {
                    alg.state = DGeneticAlgorithm.State.INITIALIZED
                    alg.thread?.join()
                    alg.thread = null
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: DGeneticAlgorithm<*>)
}
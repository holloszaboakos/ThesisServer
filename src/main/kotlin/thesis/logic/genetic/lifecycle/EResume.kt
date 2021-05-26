package thesis.logic.genetic.lifecycle

import thesis.logic.genetic.DGeneticAlgorithm
import kotlin.concurrent.thread

enum class EResume {
    STANDARD {
        override fun invoke(alg: DGeneticAlgorithm<*>) {
            when (alg.state) {
                DGeneticAlgorithm.State.CREATED -> {
                    alg.initialize()
                    alg.resume()
                }
                DGeneticAlgorithm.State.INITIALIZED -> {
                    alg.thread = thread{
                        alg.state = DGeneticAlgorithm.State.RESUMED
                        alg.run()
                    }
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: DGeneticAlgorithm<*>)
}
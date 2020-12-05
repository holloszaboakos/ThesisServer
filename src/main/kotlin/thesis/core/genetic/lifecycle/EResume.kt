package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm
import kotlin.concurrent.thread

enum class EResume {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            when (alg.state) {
                GeneticAlgorithm.State.CREATED -> {
                    alg.initialize()
                    alg.resume()
                }
                GeneticAlgorithm.State.INITIALIZED -> {
                    alg.thread = thread{
                        alg.state = GeneticAlgorithm.State.RESUMED
                        alg.run()
                    }
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
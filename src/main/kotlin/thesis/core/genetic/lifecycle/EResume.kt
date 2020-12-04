package thesis.core.genetic.lifecycle

import thesis.core.genetic.GeneticAlgorithm
import java.lang.System.currentTimeMillis

enum class EResume {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            when (alg.state) {
                GeneticAlgorithm.State.CREATED -> {
                    alg.start()
                    alg.resume()
                }
                GeneticAlgorithm.State.STARTED -> {
                    alg.run()
                }
                else -> {
                }
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
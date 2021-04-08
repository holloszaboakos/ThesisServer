package thesis.core.genetic.control

import thesis.core.genetic.EGeneticSetup
import thesis.core.genetic.GeneticAlgorithm
import thesis.core.genetic.GeneticAlgorithmSetup
import thesis.core.genetic.lifecycle.EClear
import thesis.core.genetic.lifecycle.EInitialize
import thesis.core.genetic.lifecycle.EPause
import thesis.core.genetic.lifecycle.EResume
import thesis.core.permutation.EPermutationFactory
import thesis.core.permutation.TwoPartRepresentation
import thesis.data.web.Graph

enum class ECycle {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            val oldIterationCount = alg.iteration
            alg.state = GeneticAlgorithm.State.RESUMED
            while (
                alg.runTime_Second < alg.timeLimit
                && alg.iteration < alg.iterationLimit
                && alg.iteration - oldIterationCount < alg.objectives.size
            ) {
                alg.selection()
                alg.crossover()
                alg.mutate()
                alg.orderByCost()
                alg.boost()
                alg.best = alg.population.first().copy()
                alg.worst = alg.population.last().copy()
                alg.iteration++
            }
            alg.state = GeneticAlgorithm.State.INITIALIZED
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
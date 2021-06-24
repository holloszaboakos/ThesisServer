package hu.bme.thesis.logic.genetic.control

import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.permutation.IPermutation

enum class ERunUntil {
    STANDARD {
        override fun <P:IPermutation>invoke(alg: DGeneticAlgorithm<P>) {
            alg.state = DGeneticAlgorithm.State.RESUMED
            while (
                alg.runTime_Second < alg.timeLimit
                && alg.iteration < alg.iterationLimit
                && alg.state == DGeneticAlgorithm.State.RESUMED
            ) runBlocking {
                alg.selection()
                alg.crossover()
                alg.mutate()
                alg.orderByCost()
                alg.boost()
                alg.best = alg.population.first().copy() as P
                alg.worst = alg.population.last().copy() as P
                alg.iteration++
            }
            alg.state = DGeneticAlgorithm.State.INITIALIZED
        }
    };

    abstract operator fun <P:IPermutation>invoke(alg: DGeneticAlgorithm<P>)
}
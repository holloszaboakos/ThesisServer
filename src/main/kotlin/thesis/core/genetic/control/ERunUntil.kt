package thesis.core.genetic.control

import thesis.core.genetic.GeneticAlgorithm

enum class ERunUntil {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.resumeTime = System.currentTimeMillis()
            alg.state = GeneticAlgorithm.State.RESUMED
            while (
                alg.spentTime < alg.timeLimit
                && alg.iteration < alg.iterationLimit
                && alg.state == GeneticAlgorithm.State.RESUMED
            ) {
                alg.selection()
                alg.crossover()
                alg.mutate()
                alg.orderByCost()
                alg.boost()
                alg.iteration++
                alg.spentTime += System.currentTimeMillis() - alg.resumeTime
            }
            alg.state = GeneticAlgorithm.State.STARTED
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
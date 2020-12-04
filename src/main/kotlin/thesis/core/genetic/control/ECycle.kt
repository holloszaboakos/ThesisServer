package thesis.core.genetic.control

import thesis.core.genetic.GeneticAlgorithm

enum class ECycle {
    STANDARD {
        override fun invoke(alg: GeneticAlgorithm) {
            alg.state = GeneticAlgorithm.State.RESUMED
            var cycleCounter = 0
            alg.resumeTime = System.currentTimeMillis()
            while (alg.spentTime < alg.timeLimit && alg.iteration < alg.iterationLimit && cycleCounter < alg.objectives.size) {
                alg.selection()
                alg.crossover()
                alg.mutate()
                alg.orderByCost()
                alg.boost()
                alg.iteration++
                alg.spentTime += System.currentTimeMillis() - alg.resumeTime
                cycleCounter++
            }
            alg.state = GeneticAlgorithm.State.STARTED
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm)
}
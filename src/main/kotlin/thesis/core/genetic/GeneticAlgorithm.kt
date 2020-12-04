package thesis.core.genetic

import thesis.core.Permutation
import thesis.data.web.Graph
import thesis.data.web.Objective
import thesis.data.web.Salesman
import java.math.BigDecimal

class GeneticAlgorithm(
    var timeLimit: Long = 0L,
    var iterationLimit: Int = 0,
    var costGraph: Graph,
    var objectives: Array<Objective>,
    var salesmen: Array<Salesman>,
    var setup: GeneticAlgorithmSetup
) {

    enum class State {
        CREATED,
        STARTED,
        RESUMED
    }

    var state = State.CREATED

    var iteration = 0
    var spentTime = 0L
    var resumeTime = 0L
    val population: Array<Permutation> = Array(2 * (objectives.size + 1) * objectives.size) {
        Permutation(
            Array(objectives.size) { it },
            Array(salesmen.size) { it },
            false,
            BigDecimal(-1),
            iteration = -1
        )
    }

    fun pause() = setup.pause(this)
    fun resume() = setup.resume(this)
    fun start() = setup.start(this)
    fun stop() = setup.stop(this)

    fun run() = setup.run(this)
    fun cycle() = setup.cycle(this)
    fun iterate() = setup.iteration(this)

    fun initialize() = setup.initialize(this)
    fun cost(permutation: Permutation) = setup.cost(this, permutation)
    fun orderByCost() = setup.orderByCost(this)
    fun boost() = setup.boost(this)
    fun selection() = setup.selection(this)
    fun crossover() = setup.crossover(this)
    fun mutate() = setup.mutate(this)
}
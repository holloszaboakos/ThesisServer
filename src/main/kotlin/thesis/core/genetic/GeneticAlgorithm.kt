package thesis.core.genetic

import thesis.core.Permutation
import thesis.data.web.Graph
import thesis.data.web.Objective
import thesis.data.web.Salesman
import java.math.BigDecimal
import java.util.*
import kotlin.properties.Delegates

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
        INITIALIZED,
        RESUMED
    }

    /** A futási időre vonatkozó információk. */
    private val timeOf = object {
        var running = 0L
        var resume = 0L
    }
    var thread: Thread? = null
    var state: State by Delegates.observable(State.CREATED)
    { _, oldValue, newValue ->
        when (newValue) {
            State.CREATED ->
                when (oldValue) {
                    State.CREATED -> {
                    }
                    State.INITIALIZED -> {
                        timeOf.running = 0
                        timeOf.resume = 0
                    }
                    State.RESUMED -> throw Exception("Illegal state change! from:$oldValue to:$newValue")
                }
            State.INITIALIZED ->
                when (oldValue) {
                    State.CREATED -> {
                    }
                    State.INITIALIZED -> {
                    }
                    State.RESUMED -> {
                        timeOf.running += System.currentTimeMillis() - timeOf.resume
                    }
                }
            State.RESUMED ->
                when (oldValue) {
                    State.CREATED -> throw Exception("Illegal state change! from:$oldValue to:$newValue")
                    State.INITIALIZED -> timeOf.resume = System.currentTimeMillis()
                    State.RESUMED -> {
                    }
                }
        }
    }
    val runTime_Second: Double
        get() =
            if (state != State.RESUMED)
                timeOf.running / 1000.0
            else
                (System.currentTimeMillis() - timeOf.resume + timeOf.running) / 1000.0

    var iteration = 0
    val population: Array<Permutation> = Array(2 * (objectives.size - 1) * objectives.size) {
        Permutation(
            IntArray(objectives.size) { it },
            IntArray(salesmen.size) { it },
            false,
            BigDecimal(-1),
            iteration = -1
        )
    }
    var best : Permutation? = null
    var worst : Permutation? = null

    fun pause() = setup.pause(this)
    fun resume() = setup.resume(this)
    fun initialize() = setup.initialize(this)
    fun clear() = setup.clear(this)

    fun run() = setup.run(this)
    fun cycle() = setup.cycle(this)
    fun iterate() = setup.iteration(this)

    fun initializePopulation() = setup.initializePopulation(this)
    fun cost(permutation: Permutation) = setup.cost(this, permutation)
    fun orderByCost() = setup.orderByCost(this)
    fun boost() = setup.boost(this)
    fun selection() = setup.selection(this)
    fun crossover() = setup.crossover(this)
    fun mutate() = setup.mutate(this)
}
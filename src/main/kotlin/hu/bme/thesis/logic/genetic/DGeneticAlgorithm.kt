package hu.bme.thesis.logic.genetic

import hu.bme.thesis.logic.specimen.IRepresentation
import hu.bme.thesis.logic.specimen.factory.SPermutationFactory
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DObjective
import hu.bme.thesis.model.mtsp.DSalesman
import kotlin.properties.Delegates

data class DGeneticAlgorithm<P : IRepresentation>(
    var permutationFactory: SPermutationFactory<P>,
    var timeLimit: Long = 0L,
    var iterationLimit: Int = 0,
    var costGraph: DGraph,
    var objectives: Array<DObjective>,
    var salesmen: Array<DSalesman>,
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
    var population: ArrayList<P> = if (objectives.size != 1)
        ArrayList(List(4 * (objectives.size + salesmen.size)){
            permutationFactory.produce(
                Array(salesmen.size) { index ->
                    if (index == 0)
                        IntArray(objectives.size) { it }
                    else
                        intArrayOf()
                }
            )
        })
    else arrayListOf(
        permutationFactory.produce(
            arrayOf(IntArray(objectives.size) { it })
        )
    )
    var best: P? = null
    var worst: P? = null

    fun pause() = setup.pause(this)
    fun resume() = setup.resume(this)
    fun initialize() = setup.initialize(this)
    fun clear() = setup.clear(this)

    fun run() = setup.run(this)
    fun cycle() = setup.cycle(this)
    fun iterate() = setup.iteration(this)

    suspend fun initializePopulation() = setup.initializePopulation(this)
    fun cost(permutation: P) = setup.cost(this, permutation)
    suspend fun orderByCost() = setup.orderByCost(this)
    fun boost() = setup.boost(this)
    val selection by lazy { setup.selection(this) }
    suspend fun crossover() = setup.crossover(this)
    fun crossoverOperator(
        parents: Pair<P, P>,
        child: P
    ) = setup.crossoverOperator(parents, child, this)

    suspend fun mutate() = setup.mutate(this)
}
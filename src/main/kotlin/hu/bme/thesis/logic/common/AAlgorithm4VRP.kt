package hu.bme.thesis.logic.common

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.inner.setup.AAlgorithm4VRPSetup
import hu.bme.thesis.model.mtsp.DEdge
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DObjective
import hu.bme.thesis.model.mtsp.DSalesman
import kotlinx.coroutines.Job
import kotlin.properties.Delegates

abstract class AAlgorithm4VRP<S : ISpecimenRepresentation>(
    open val subSolutionFactory: SSpecimenRepresentationFactory<S>,
    open val costGraph: DGraph,
    open val salesmen: Array<DSalesman>,
    open val setup: AAlgorithm4VRPSetup,
    open val timeLimit: Long,
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

    var job: Job? = null

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



    suspend fun pause() = setup.pause(this)
    suspend fun resume() = setup.resume(this)
    fun initialize() = setup.initialize(this)
    fun clear() = setup.clear(this)

    fun cost(specimen: S) = setup.cost(this, specimen)
    fun costOfEdge(edge: DEdge, salesman: DSalesman) = setup.costOfEdge(edge, salesman)
    fun costOfObjective(objective: DObjective, salesman: DSalesman) = setup.costOfObjective(objective, salesman)

    abstract suspend fun run():S
}
package hu.bme.thesis.logic

import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.evolutionary.setup.EGeneticSetup
import hu.bme.thesis.logic.specimen.factory.OOnePartRepresentationFactory
import hu.bme.thesis.model.inner.setup.AAlgorithm4VRPSetup
import hu.bme.thesis.model.mtsp.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.math.BigDecimal


object OAlgorithmManager {

    var algorithm: GeneticAlgorithm<*>? = null
    var task: DTask? = null
    var settings: DSetting? = null
    private var minCost: Double = (Double.MAX_VALUE)
    private var maxCost: Double = (Double.MIN_VALUE)
    private val lock = Mutex()

    fun prepare(): Boolean = runBlocking {
        lock.withLock {
            task?.let { task ->
                task.costGraph.edgesBetween.size == task.costGraph.objectives.size
                        && task.costGraph.edgesBetween.all { it.values.size == task.costGraph.objectives.size - 1 }
                        && task.costGraph.edgesFromCenter.size == task.costGraph.objectives.size
                        && task.costGraph.edgesToCenter.size == task.costGraph.objectives.size
            } == true
        }
    }


    fun start() = runBlocking {
        lock.withLock {
            task?.let { task ->
                settings?.let { settings ->
                    algorithm = GeneticAlgorithm(
                        OOnePartRepresentationFactory,
                        (settings.timeLimit_Second * BigDecimal(1000)).toLong(),
                        settings.iterLimit.toInt(),
                        task.costGraph,
                        task.salesmen,
                        EGeneticSetup.values()
                            .find { it.code.compareTo(settings.algorithm) == 0 }?.setup
                            ?: throw Exception("There is no setup with given name: ${settings.algorithm}")
                    )
                }
            }
            algorithm?.initialize()
        }
    }

    fun resume() = runBlocking {
        lock.withLock {
            algorithm?.resume()
        }
    }

    fun pause() = runBlocking {
        lock.withLock {
            algorithm?.pause()
        }
    }

    fun stop() = runBlocking {
        lock.withLock {
            algorithm?.clear()
        }
    }

    fun clean() = runBlocking {
        lock.withLock {
            algorithm = null
            task = null
            settings = null
            minCost = (Double.MAX_VALUE)
            maxCost = (Double.MIN_VALUE)
        }
    }

    fun calcResult(): DResult = runBlocking {
        lock.withLock {
            task?.run {
                if (minCost == (Double.MAX_VALUE)) {
                    var minLength_Meter = 0L
                    costGraph.edgesBetween.forEachIndexed { index, edgeArray ->
                        minLength_Meter += minOf(
                            edgeArray.values.minOf { it.length_Meter },
                            costGraph.edgesToCenter[index].length_Meter
                        )
                    }
                    minCost = salesmen.minOf {
                        it.basePrice_Euro +
                                costGraph.objectives.sumOf { objective -> objective.time_Second * it.payment_EuroPerSecond } +
                                minLength_Meter * it.fuelConsuption_LiterPerMeter * it.fuelPrice_EuroPerLiter +
                                minLength_Meter.toDouble() / it.vechicleSpeed_MeterPerSecond * it.payment_EuroPerSecond
                    }
                }

                if (maxCost == (Double.MIN_VALUE)) {
                    val maxLength_Meter =
                        costGraph.edgesFromCenter.sumOf { it.length_Meter } + costGraph.edgesToCenter.sumOf { it.length_Meter }
                    maxCost = salesmen.minOf { salesman ->
                        salesman.basePrice_Euro +
                                costGraph.objectives.sumOf { objective -> objective.time_Second * salesman.payment_EuroPerSecond } +
                                maxLength_Meter * salesman.fuelConsuption_LiterPerMeter * salesman.fuelPrice_EuroPerLiter +
                                maxLength_Meter.toDouble() / salesman.vechicleSpeed_MeterPerSecond * salesman.payment_EuroPerSecond
                    }
                }
            }
            algorithm?.run {

                val bestRout: Array<DGpsArray> =
                    best?.mapSlice { slice ->
                        val gpsList = slice.map { value -> costGraph.objectives[value.toInt()].location }.toList()
                        DGpsArray(values = gpsList.toTypedArray())
                    }?.toList()?.toTypedArray() ?: arrayOf()
                worst?.let { worst ->
                    if (worst.cost < maxCost) {
                        maxCost = worst.cost
                    }
                }

                DResult(
                    name = "",
                    bestRout = bestRout,
                    maxCost_Euro = maxCost,
                    minCost_Euro = minCost,
                    bestCost_Euro = best?.cost ?: maxCost
                )
            } ?: task?.run {
                DResult(
                    name = "",
                    bestRout = arrayOf(),
                    maxCost_Euro = maxCost,
                    minCost_Euro = minCost,
                    bestCost_Euro = maxCost
                )
            } ?: throw Exception("Funcion Should Not be called if algorithm and task are null")

        }
    }

    fun step(): DResult = runBlocking {
        lock.withLock {
            algorithm?.iterate(true)
            calcResult()
        }
    }

    fun iterate() = runBlocking {
        lock.withLock {
            algorithm?.iterate(true)
        }
    }

    fun cycle(): DResult = runBlocking {
        lock.withLock {
            algorithm?.cycle()
            calcResult()
        }
    }
}
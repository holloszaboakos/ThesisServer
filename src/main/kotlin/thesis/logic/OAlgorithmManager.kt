package thesis.logic

import thesis.logic.genetic.EGeneticSetup
import thesis.logic.genetic.DGeneticAlgorithm
import thesis.logic.permutation.DTwoPartRepresentation
import thesis.model.mtsp.*

import java.math.BigDecimal


object OAlgorithmManager {

    var algorithm: DGeneticAlgorithm<*>? = null
    var task: DTask? = null
    var settings: DSetting? = null
    private var minCost: BigDecimal = BigDecimal(Long.MAX_VALUE)
    private var maxCost: BigDecimal = BigDecimal(Long.MIN_VALUE)

    fun prepare(): Boolean =
        task?.let { task ->
            task.costGraph.edgesBetween.size == task.costGraph.objectives.size
                    && task.costGraph.edgesBetween.all { it.values.size == task.costGraph.objectives.size - 1 }
                    && task.costGraph.edgesFromCenter.size == task.costGraph.objectives.size
                    && task.costGraph.edgesToCenter.size == task.costGraph.objectives.size
        } == true


    fun start() {
        task?.let { task ->
            settings?.let { settings ->
                algorithm = DGeneticAlgorithm<DTwoPartRepresentation>(
                    (settings.timeLimit_Second * BigDecimal(1000)).toLong(),
                    settings.iterLimit.toInt(),
                    task.costGraph,
                    task.costGraph.objectives,
                    task.salesmen,
                    EGeneticSetup.values()
                        .find { it.code.compareTo(settings.algorithm) == 0 }?.setup
                        ?: throw Exception("There is no setup with given name")
                )
            }
        }
        algorithm?.initialize()
    }

    fun resume() {
        algorithm?.resume()
    }

    fun pause() {
        algorithm?.pause()
    }

    fun stop() {
        algorithm?.clear()
    }

    fun clean() {
        algorithm = null
        task = null
        settings = null
        minCost = BigDecimal(Long.MAX_VALUE)
        maxCost = BigDecimal(Long.MIN_VALUE)
    }

    fun calcResult(): DResult {
        task?.run {
            if (minCost == BigDecimal(Long.MAX_VALUE)) {
                var minLength_Meter = BigDecimal(0)
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
                            minLength_Meter / it.vechicleSpeed_MeterPerSecond * it.payment_EuroPerSecond
                }
            }

            if (maxCost == BigDecimal(Long.MIN_VALUE)) {
                val maxLength_Meter =
                    costGraph.edgesFromCenter.sumOf { it.length_Meter } + costGraph.edgesToCenter.sumOf { it.length_Meter }
                maxCost = salesmen.minOf { salesman ->
                    salesman.basePrice_Euro +
                            costGraph.objectives.sumOf { objective -> objective.time_Second * salesman.payment_EuroPerSecond } +
                            maxLength_Meter * salesman.fuelConsuption_LiterPerMeter * salesman.fuelPrice_EuroPerLiter +
                            maxLength_Meter / salesman.vechicleSpeed_MeterPerSecond * salesman.payment_EuroPerSecond
                }
            }
        }
        return algorithm?.run {

            val bestRout: Array<DGpsArray> =
                best?.mapSlice { slice ->
                    val gpsList = slice.map { value -> objectives[value].location }
                    DGpsArray(values = gpsList.toTypedArray())
                }?.toTypedArray() ?: arrayOf()
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

    fun step(): DResult {
        algorithm?.iterate()
        return calcResult()
    }

    fun cycle(): DResult {
        algorithm?.cycle()
        return calcResult()
    }
}
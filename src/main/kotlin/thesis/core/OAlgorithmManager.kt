package thesis.core

import thesis.core.genetic.EGeneticSetup
import thesis.core.genetic.GeneticAlgorithm
import thesis.data.web.*

import java.math.BigDecimal


object OAlgorithmManager {

    var algorithm: GeneticAlgorithm? = null
    var task: Task? = null
    var settings: Setting? = null
    var minCost: BigDecimal = BigDecimal(Long.MAX_VALUE)

    fun prepare(): Boolean =
        task?.let { task ->
            task.costGraph.edgesBetween.size == task.costGraph.objectives.size
                    && task.costGraph.edgesBetween.all { it.values.size == task.costGraph.objectives.size }
                    && task.costGraph.edgesFromCenter.size == task.costGraph.objectives.size
                    && task.costGraph.edgesToCenter.size == task.costGraph.objectives.size
        } == true


    fun start() {
        task?.let { task ->
            settings?.let { settings ->
                algorithm = GeneticAlgorithm(
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
        algorithm?.start()
    }

    fun resume() {
        algorithm?.resume()
    }

    fun pause() {
        algorithm?.pause()
    }

    fun stop() {
        algorithm?.stop()
    }

    fun clean() {
        algorithm = null
        task = null
        settings = null
    }

    fun calcResult(): Result {
        var result: Result? = null
        algorithm?.apply {

            if (minCost == BigDecimal(Long.MAX_VALUE)) {
                var minLength_Meter = BigDecimal(0)
                costGraph.edgesBetween.forEach { edgeArray ->
                    minLength_Meter += edgeArray.values.minOf { it.length_Meter }
                }
                minCost = salesmen.minOf {
                    var cost = it.basePrice_Euro
                    costGraph.objectives.forEach { objective ->
                        cost += objective.time_Second * it.payment_EuroPerSecond
                    }
                    cost += minLength_Meter * it.fuelConsuption_LiterPerMeter * it.fuelPrice_EuroPerLiter
                    cost += minLength_Meter / it.vechicleSpeed_MeterPerSecond * it.payment_EuroPerSecond
                    cost
                }
            }

            val bestRout: Array<GpsArray> =
                population.first().run {
                    var geneIndex = 0
                    sliceLengthes.map { sliceLength ->
                        val gpsList = (geneIndex until (geneIndex + sliceLength))
                            .map { index -> objectives[values[index]].location }
                        geneIndex += sliceLength
                        GpsArray(values = gpsList.toTypedArray())
                    }
                }.toTypedArray()

            result = Result(
                name = "",
                bestRout = bestRout,
                maxCost_Euro = population.last().cost,
                minCost_Euro = minCost,
                bestCost_Euro = population.first().cost
            )
        } ?: task?.apply {

            if (minCost == BigDecimal(Long.MAX_VALUE)) {
                var minLength_Meter = BigDecimal(0)
                costGraph.edgesBetween.forEach { edgeArray ->
                    minLength_Meter += edgeArray.values.minOf { it.length_Meter }
                }
                minCost = salesmen.minOf {
                    var cost = it.basePrice_Euro
                    costGraph.objectives.forEach { objective ->
                        cost += objective.time_Second * it.payment_EuroPerSecond
                    }
                    cost += minLength_Meter * it.fuelConsuption_LiterPerMeter * it.fuelPrice_EuroPerLiter
                    cost += minLength_Meter / it.vechicleSpeed_MeterPerSecond * it.payment_EuroPerSecond
                    cost
                }
            }

            val maxLength = costGraph.edgesFromCenter.sumOf { it.length_Meter } + costGraph.edgesToCenter.sumOf { it.length_Meter }
            val maxCost = salesmen.maxOf { salesman ->
                maxLength / salesman.vechicleSpeed_MeterPerSecond * salesman.payment_EuroPerSecond +
                        costGraph.objectives.sumOf { it.time_Second } * salesman.payment_EuroPerSecond +
                        salesman.basePrice_Euro +
                        maxLength * salesman.fuelConsuption_LiterPerMeter + salesman.fuelPrice_EuroPerLiter
            }

            result = Result(
                name = "",
                bestRout = arrayOf(),
                maxCost_Euro = maxCost,
                minCost_Euro = minCost,
                bestCost_Euro = maxCost
            )
        }
        return result ?: throw Exception("Funcion Should Not be called if algorithm is null")
    }

    fun step(): Result {
        algorithm?.iterate()
        return calcResult()
    }

    fun cycle(): Result {
        algorithm?.cycle()
        return calcResult()
    }
}
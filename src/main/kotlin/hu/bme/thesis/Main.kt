package hu.bme.thesis

import com.google.gson.Gson
import hu.bme.thesis.logic.OAlgorithmManager
import hu.bme.thesis.logic.common.steps.ECost
import hu.bme.thesis.logic.evolutionary.genetic.ECrossOverOperator
import hu.bme.thesis.model.mtsp.*
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.Error
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

//record 44355.0
//objective 1000, boost: otp_step, mutation:reset, population: 4 * customer : 193575
//objective 1000, boost: otp_step, mutation:reset, population: 4 * customer : 184957

//objective 1000, boost: none, operator:heuristic , mutation:reverse, population: 4 * customer : 162960.0 in 1700 iter
//objective 1000,  boost: none, operator:heuristic , mutation:reverse, population: 4 * ( customer + transport unit)
//step:10280 timeElapsed: 6635.5869, bestCost 152902.0, worstCost 2.9090193731010694E17
//objective 1000, boost: otp_step, mutation:reset, population: 4 * customer : 184957
//step:10601 timeElapsed: 1459, bestCost 168340.0, worstCost 7.5460522555858202E17

//HEURISTIC
//step:1303 timeElapsed: 865, bestCost 81704.0, worstCost 2.08111210262199552E17
//step:11664 timeElapsed: 2935, bestCost 62372.0, worstCost 2.64034600537264864E17
//step:25000 timeElapsed: 2935, bestCost 59024.0, worstCost 2.64034600537264864E17

@ExperimentalTime
fun main(args: Array<String>) {
    val argMap = mutableMapOf<String, String>()
    for (index in 0 until args.size / 2) {
        argMap[args[index * 2]] = args[index * 2 + 1]
    }
    val setupFilePath = argMap["-setupFilePath"] ?: throw Error("No inputFilePath given")
    val betweenFilePath = argMap["-betweenFilePath"] ?: throw Error("No inputFilePath given")
    val fromCenterFilePath = argMap["-fromCenterFilePath"] ?: throw Error("No inputFilePath given")
    val toCenterFilePath = argMap["-toCenterFilePath"] ?: throw Error("No inputFilePath given")
    val salesmanFilePath = argMap["-salesmanFilePath"] ?: throw Error("No inputFilePath given")
    val objectivesFilePath = argMap["-objectivesFilePath"] ?: throw Error("No inputFilePath given")
    val outputFolderPath = argMap["-outputFolderPath"] ?: throw Error("No inputFilePath given")

    val setupFile = File(setupFilePath)
    val betweenFile = File(betweenFilePath)
    val fromCenterFile = File(fromCenterFilePath)
    val toCenterFile = File(toCenterFilePath)
    val salesmanFile = File(salesmanFilePath)
    val objectivesFile = File(objectivesFilePath)

    val gson = Gson()
    var incompleteSetup: DSetup? = gson.fromJson(setupFile.readText(), DSetup::class.java)
    var edgesBetween: Array<DEdgeArray>? = gson.fromJson(betweenFile.readText(), Array<DEdgeArray>::class.java)
    var edgesFromCenter: Array<DEdge>? = gson.fromJson(fromCenterFile.readText(), Array<DEdge>::class.java)
    var edgesToCenter: Array<DEdge>? = gson.fromJson(toCenterFile.readText(), Array<DEdge>::class.java)
    var salesmen: Array<DSalesman>? = gson.fromJson(salesmanFile.readText(), Array<DSalesman>::class.java)
    var objectives: Array<DObjective>? = gson.fromJson(objectivesFile.readText(), Array<DObjective>::class.java)

    val setup = incompleteSetup?.let { ics ->
        ics.copy(
            task = ics.task.copy(
                salesmen = salesmen ?: throw Error("WTF"),
                costGraph = ics.task.costGraph.copy(
                    objectives = objectives ?: throw Error("WTF"),
                    edgesBetween = edgesBetween ?: throw Error("WTF"),
                    edgesFromCenter = edgesFromCenter ?: throw Error("WTF"),
                    edgesToCenter = edgesToCenter ?: throw Error("WTF")
                )
            )
        )
    } ?: throw Error("WTF")

    edgesBetween = null
    edgesFromCenter = null
    edgesToCenter = null
    salesmen = null
    objectives = null

    OAlgorithmManager.task = setup.task
    OAlgorithmManager.settings = setup.setting

    OAlgorithmManager.prepare()
    OAlgorithmManager.start()
    val outputFile = File("$outputFolderPath\\statistics.txt")
    for (index in 0 until 500){//setup.task.costGraph.objectives.size * setup.task.costGraph.objectives.size * setup.task.costGraph.objectives.size) {
        val duration = measureTime {
            runBlocking {
                OAlgorithmManager.iterate()
            }
        }
        val bestCost = OAlgorithmManager.algorithm?.best?.cost ?: -1
        val worstCost = OAlgorithmManager.algorithm?.worst?.cost ?: -1
        println("step:$index timeElapsed: ${duration.toLong(DurationUnit.MILLISECONDS)}, bestCost $bestCost, worstCost $worstCost")
        println("fitness cost call count: ${ECost.fitnessCallCount}")

        outputFile.appendText("\nstep:$index timeElapsed: ${duration.toDouble(DurationUnit.MILLISECONDS)}, bestCost $bestCost, worstCost $worstCost")
        ECrossOverOperator.STATISTICAL_RACE.operators.entries
            .sortedBy { it.value.successRatio }
            .forEach { (operator, statistics) ->
                println("name: ${operator.name}, value: $statistics")
                //outputFile.appendText("\nname: ${operator.name}, value: $statistics")
            }
        println()
    }
}
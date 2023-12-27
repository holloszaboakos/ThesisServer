package hu.bme.thesis.logic.linkernighan

import com.google.gson.Gson
import hu.bme.thesis.logic.specimen.factory.OOnePartRepresentationFactory
import hu.bme.thesis.model.mtsp.*
import java.io.File

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
    //val outputFolderPath = argMap["-outputFolderPath"] ?: throw Error("No inputFilePath given")

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

    betweenFile.writeText(
        gson.toJson(edgesBetween)
    )

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

    /*
    val lk = LinKernighanTSP(
        OOnePartRepresentationFactory,
        setup.task.costGraph.objectives.map { it.location }.toTypedArray(),
        setup.task.costGraph.objectives.size,
        setup.task.salesmen,
        setup.task.costGraph
    )
     */
    //lk.runAlgorithm()
    //println(lk.result.cost)
}
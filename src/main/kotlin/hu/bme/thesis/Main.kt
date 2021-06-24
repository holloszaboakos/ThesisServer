package hu.bme.thesis

import com.google.gson.Gson
import hu.bme.thesis.logic.OAlgorithmManager
import hu.bme.thesis.model.mtsp.DSetup
import java.io.File
import java.lang.Error
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main(args:Array<String>){
    val  argMap = mutableMapOf<String,String>()
    for(index in 0 until  args.size / 2){
        argMap[args[index*2]] = args[index*2+1]
    }
    val inputFilePath = argMap["-inputFilePath"] ?: throw Error("No inputFilePath given")
    val outputFolderPath = argMap["-outputFolderPath"] ?: throw Error("No outputFolderPath given")

    val inputFile = File(inputFilePath)
    val gson = Gson()
    val setup: DSetup = gson.fromJson(inputFile.readText(), DSetup::class.java)
    OAlgorithmManager.task = setup.task
    OAlgorithmManager.settings = setup.setting
    OAlgorithmManager.prepare()
    OAlgorithmManager.start()
    val outputFile = File("$outputFolderPath\\statistics.txt")
    for (index in 0 until setup.task.costGraph.objectives.size){
        val duration = measureTime {
            OAlgorithmManager.cycle()
        }
        val cost = OAlgorithmManager.algorithm?.best?.cost ?: -1
        outputFile.appendText("cycle:$index timeElapsed: ${duration.inMilliseconds}, bestCost $cost")

    }

}
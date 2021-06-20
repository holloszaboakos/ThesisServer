package thesis

import com.google.gson.Gson
import thesis.logic.OAlgorithmManager
import thesis.model.mtsp.DSetup
import java.io.File
import java.lang.Error
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main(){
    println("Give input file path:")
    val inputFilePath = readLine() ?: throw Error("No path given")
    println("Give output folder path:")
    val outputFolderPath = readLine() ?: throw Error("No path given")

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
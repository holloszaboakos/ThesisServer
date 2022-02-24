package hu.bme.thesis

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import hu.bme.thesis.model.mtsp.*
import hu.bme.thesis.utility.requestRoot
import java.io.File
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

fun main() {
    val sourceFile = File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\wapromAddress.csv")
    val fileForEdgesFromCenter =
        File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\hungarianExample\\fromCenter.json")
    val fileForEdgesToCenter =
        File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\hungarianExample\\toCenter.json")
    val fileForEdgesBetween =
        File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\hungarianExample\\between.json")
    val fileForSalesman =
        File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\hungarianExample\\salesman.json")
    val fileForObjectives =
        File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\hungarianExample\\objectives.json")
    val fileForSetup =
        File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\hungarianExample\\setup.json")

    var header: List<String>
    var indexOfLocationLatitude = 0
    var indexOfLocationLongitude = 0
    var firstLine = true
    val gpses = mutableListOf<DGps>()
    var i = 0
    sourceFile.forEachLine { line ->
        if (firstLine) {
            header = line.split(';')
            indexOfLocationLatitude = header.indexOf(header.find { it.compareTo("locationLatitude") == 0 })
            indexOfLocationLongitude = header.indexOf(header.find { it.compareTo("locationLongitude") == 0 })
            firstLine = !firstLine
        } else if (i < 1000) {
            val data = line.split(';')
            if (data[indexOfLocationLatitude].compareTo("NULL") != 0) {
                val latitude = data[indexOfLocationLatitude].toFloat()
                val longitude = data[indexOfLocationLongitude].toFloat()
                gpses.add(DGps(latitude = latitude, longitude = longitude))
                i++
            }
        }
    }
    val center = gpses.first()
    gpses.removeAt(0)

    var failCounter = 0
    var counter = 0
    gpses.removeIf {
        println("$counter. edgefrom center, $failCounter fail so far, out of ${gpses.size} ")
        counter++
        val failed = planRout(it, center) == null || planRout(center, it) == null
        if (failed)
            failCounter++
        failed
    }
    /*
    writeJsonStreamToFile(fileForObjectives, sequence {
        gpses.mapIndexed { index, gps ->
            println("objective $index out of ${gpses.size} ")
            yield(
                DObjective(
                    name = "hungarian example objective $index",
                    location = gps,
                    orderInOwner = index,
                    weight_Gramm = (Random.nextLong(1000, 100000)),
                    volume_Stere = (Random.nextLong(1000, 100000)),
                    time_Second = (Random.nextLong(60, 600)),
                )
            )
        }
    })

    writeJsonStreamToFile(fileForEdgesFromCenter, sequence {
        gpses.mapIndexed { index, gps ->
            println("fromCenter $index out of ${gpses.size} ")
            yield(
                planRout(center, gps) ?: DEdge(length_Meter = Long.MAX_VALUE,orderInOwner = index)
            )
        }
    })

    writeJsonStreamToFile(fileForEdgesToCenter, sequence {
        gpses.mapIndexed { index, gps ->
            println("toCenter $index out of ${gpses.size} ")
            yield(
                planRout(gps, center) ?: DEdge(length_Meter = Long.MAX_VALUE,orderInOwner = index)
            )
        }
    })

     */

    writeJsonStreamToFile(fileForEdgesBetween, sequence {
        gpses.mapIndexed { fromIndex, fromGps ->
            if (fromIndex > 690)
                yield(DEdgeArray(
                    orderInOwner = fromIndex,
                    values = gpses
                        .filter { it != fromGps }
                        .mapIndexed { toIndex, toGps ->
                            println("between from $fromIndex to $toIndex out of ${gpses.size} ")
                            planRout(fromGps, toGps) ?: DEdge(length_Meter = Long.MAX_VALUE, orderInOwner = toIndex)
                        }
                        .toTypedArray()
                ))
        }
    }, false)

    writeJsonStreamToFile(fileForSalesman, sequence {
        (gpses.indices).forEach { index ->
            yield(
                DSalesman(
                    name = "hungarian example salesman $index",
                    orderInOwner = index,
                    workTime_SecondPerDay = (60 * 60 * 8L),
                    weightCapacity_Gramm = (Random.nextLong(10_000, 1000_000)),
                    volumeCapacity_Stere = (Random.nextLong(10_000, 1000_000)),
                    vechicleSpeed_MeterPerSecond = (Random.nextLong(25, 100)),
                    fuelPrice_EuroPerLiter = (Random.nextDouble(1.0, 2.0)),
                    payment_EuroPerSecond = (Random.nextDouble(0.001, 0.002)),
                    fuelConsuption_LiterPerMeter = (Random.nextDouble(0.005, 0.01)),
                    basePrice_Euro = (0.0)
                )
            )
        }
    })

    val setup = DSetup(
        task = DTask(
            name = "hungarian example task",
            salesmen = arrayOf(),
            costGraph = DGraph(
                name = "hungarian example cost graph",
                objectives = arrayOf(),
                center = center,
                edgesBetween = arrayOf(),
                edgesFromCenter = arrayOf(),
                edgesToCenter = arrayOf(),
            ),
        ),
        setting = DSetting(
            name = "hungarian example setup",
            timeLimit_Second = BigDecimal(60 * 60 * 8),
            iterLimit = BigDecimal(Int.MAX_VALUE),
            algorithm = "statisticalRaceBased",
        ),
    )
    writeJsonToFile(fileForSetup, setup)
}

fun planRout(from: DGps, to: DGps): DEdge? {
    var response = requestRoot(
        from.latitude.toDouble(),
        from.longitude.toDouble(),
        to.latitude.toDouble(),
        to.longitude.toDouble()
    )

    repeat(3) {
        if (response.plan?.itineraries?.get(0)?.legs?.get(0)?.legGeometry?.points == null) {
            response = requestRoot(
                from.latitude.toDouble(),
                from.longitude.toDouble(),
                to.latitude.toDouble(),
                to.longitude.toDouble()
            )
        }
    }

    response.plan?.itineraries?.get(0)?.legs?.get(0)?.let { leg ->
        leg.legGeometry?.points?.let {
            leg.distance?.let { distance ->
                return DEdge(
                    UUID.randomUUID().toString(),
                    "hungarian example from: (${from.latitude},${from.longitude}) to: (${to.latitude},${to.longitude})",
                    0,
                    (distance.toLong()),
                    route = arrayOf()//PolylineEncoder.decode(points).toTypedArray()
                )
            }
        }
    }

    return null
}

fun writeJsonToFile(file: File, value: Any) {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val uglyJsonString = gson.toJson(value)
    val jsonElement: JsonElement = JsonParser.parseString(uglyJsonString)
    file.writeText(gson.toJson(jsonElement))
}

fun writeJsonStreamToFile(file: File, values: Sequence<Any>, rewrite: Boolean = true) {
    val gson = GsonBuilder().setPrettyPrinting().create()
    if (rewrite)
        file.writeText("[")
    values.forEachIndexed { index, value ->
        if (index != 0)
            file.appendText(",\n")
        val uglyJsonString = gson.toJson(value)
        val jsonElement: JsonElement = JsonParser.parseString(uglyJsonString)
        file.appendText(gson.toJson(jsonElement))
    }
    file.appendText("\n]")
}
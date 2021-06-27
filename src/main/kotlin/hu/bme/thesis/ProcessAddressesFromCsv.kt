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
    val f = File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\wapromAddress.csv")
    var header: List<String>
    var indexOfLocationLatitude = 0
    var indexOfLocationLongitude = 0
    var firstLine = true
    val gps = mutableListOf<DGps>()
    var i = 0
    f.forEachLine { line ->
        if (firstLine) {
            header = line.split(';')
            indexOfLocationLatitude = header.indexOf(header.find { it.compareTo("locationLatitude") == 0 })
            indexOfLocationLongitude = header.indexOf(header.find { it.compareTo("locationLongitude") == 0 })
            firstLine = !firstLine
        } else if (i < 100) {
            val data = line.split(';')
            if (data[indexOfLocationLatitude].compareTo("NULL") != 0) {
                val latitude = data[indexOfLocationLatitude].toFloat()
                val longitude = data[indexOfLocationLongitude].toFloat()
                gps.add(DGps(lattitude = latitude, longitude = longitude))
                i++
            }
        }
    }
    val center = gps.first()
    gps.removeAt(0)

    var failCounter = 0
    var counter = 0
    gps.removeIf {
        println("$counter. edgefrom center, $failCounter fail so far, out of ${gps.size} ")
        counter++
        val failed = planRout(it, center) == null || planRout(center, it) == null
        if (failed)
            failCounter++
        failed
    }
    val objectives = gps.mapIndexed { index, value ->
        DObjective(
            name = "hungarian example objective $index",
            location = value,
            orderInOwner = index,
            weight_Gramm = (Random.nextLong(1000, 100000)),
            volume_Stere = (Random.nextLong(1000, 100000)),
            time_Second = (Random.nextLong(60, 600)),
        )
    }.toTypedArray()
    val fromCenter = gps.mapIndexed { index, it ->
        println("fromCenter $index out of ${gps.size} ")
        planRout(center, it) ?: DEdge(length_Meter = Long.MAX_VALUE)
    }.toTypedArray()
    val toCenter = gps.mapIndexed { index, it ->
        println("toCenter $index out of ${gps.size} ")
        planRout(it, center) ?: DEdge(length_Meter = Long.MAX_VALUE)
    }.toTypedArray()
    val between = gps.mapIndexed { index, value ->
        DEdgeArray(
            orderInOwner = index,
            values = gps
                .filter { it != value }
                .mapIndexed { indexInner, it ->
                    println("between from $index to $indexInner out of ${gps.size} ")
                    planRout(value, it) ?: DEdge(length_Meter = Long.MAX_VALUE)
                }
                .toTypedArray()
        )
    }.toTypedArray()

    val salesmen = Array(gps.size) { index ->
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
    }

    val result = DSetup(
        task = DTask(
            name = "hungarian example task",
            salesmen = salesmen,
            costGraph = DGraph(
                name = "hungarian example cost graph",
                objectives = objectives,
                center = center,
                edgesBetween = between,
                edgesFromCenter = fromCenter,
                edgesToCenter = toCenter,
            ),
        ),
        setting = DSetting(
            name = "hungarian example setup",
            timeLimit_Second = BigDecimal(60 * 60 * 8),
            iterLimit = BigDecimal(Int.MAX_VALUE),
            algorithm = "statisticalRaceBased",
        ),
    )

    val file = File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\hungarianExample.json")
    val gson = GsonBuilder().setPrettyPrinting().create()
    val uglyJsonString = gson.toJson(result)
    val je: JsonElement = JsonParser.parseString(uglyJsonString)
    file.writeText(gson.toJson(je))
}

fun planRout(from: DGps, to: DGps): DEdge? {
    var response = requestRoot(
        from.lattitude.toDouble(),
        from.longitude.toDouble(),
        to.lattitude.toDouble(),
        to.longitude.toDouble()
    )

    repeat(10) {
        if (response.plan?.itineraries?.get(0)?.legs?.get(0)?.legGeometry?.points == null) {
            response = requestRoot(
                from.lattitude.toDouble(),
                from.longitude.toDouble(),
                to.lattitude.toDouble(),
                to.longitude.toDouble()
            )
        }
    }

    response.plan?.itineraries?.get(0)?.legs?.get(0)?.let { leg ->
        leg.legGeometry?.points?.let { points ->
            leg.distance?.let { distance ->
                return DEdge(
                    UUID.randomUUID().toString(),
                    "hungarian example from: (${from.lattitude},${from.longitude}) to: (${to.lattitude},${to.longitude})",
                    0,
                    (distance.toLong()),
                    rout = arrayOf()//PolylineEncoder.decode(points).toTypedArray()
                )
            }
        }
    }

    return null
}
package hu.bme.thesis

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import hu.bme.thesis.model.mtsp.*
import java.io.File
import java.math.BigDecimal


//D:\Git\GitHub\SourceCodes\Kotlin\Web\ThesisServer\output
fun main() {
    val file = File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\example.json")
    val gson = GsonBuilder().setPrettyPrinting().create()
    val uglyJsonString = gson.toJson(
        DSetup(
            DTask(
                name = "exampleTask",
                costGraph = DGraph(
                    center = DGps(
                        latitude = 0.0f,
                        longitude = 0.0f,
                    ),
                    objectives = arrayOf(
                        DObjective(
                            name = "exampleObjective",
                            location = DGps(
                                latitude = 0.0f,
                                longitude = 0.0f,
                            ),
                            time_Second = 0L,
                            volume_Stere = 0L,
                            weight_Gramm = 0L,
                        )
                    ),
                    edgesToCenter = arrayOf(
                        DEdge(
                            name = "exampleEdges0ToCenter",
                            length_Meter = 0L,
                            route = arrayOf(
                                DGps(
                                    latitude = 0.0f,
                                    longitude = 0.0f
                                )
                            )
                        ),
                        DEdge(
                            name = "exampleEdges1ToCenter",
                            length_Meter = 0L,
                            route = arrayOf(
                                DGps(
                                    latitude = 0.0f,
                                    longitude = 0.0f
                                )
                            )
                        ),
                    ),
                    edgesFromCenter = arrayOf(
                        DEdge(
                            name = "exampleEdges0FromCenter",
                            length_Meter = 0L,
                            route = arrayOf(
                                DGps(
                                    latitude = 0.0f,
                                    longitude = 0.0f
                                )
                            )
                        ),
                        DEdge(
                            name = "exampleEdges1FromCenter",
                            length_Meter = 0L,
                            route = arrayOf(
                                DGps(
                                    latitude = 0.0f,
                                    longitude = 0.0f
                                )
                            )
                        ),
                    ),
                    edgesBetween = arrayOf(
                        DEdgeArray(
                            values = arrayOf(
                                DEdge(
                                    name = "exampleEdgesBetween0to1",
                                    length_Meter = 0L,
                                    route = arrayOf(
                                        DGps(
                                            latitude = 0.0f,
                                            longitude = 0.0f
                                        )
                                    )
                                ),
                            )
                        ),
                        DEdgeArray(
                            values = arrayOf(
                                DEdge(
                                    name = "exampleEdgesBetween1to0",
                                    length_Meter = 0L,
                                    route = arrayOf(
                                        DGps(
                                            latitude = 0.0f,
                                            longitude = 0.0f
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                salesmen = arrayOf(
                    DSalesman(
                        name = "exampleSalesman",
                        basePrice_Euro = 0.0,
                        fuelConsuption_LiterPerMeter = 0.0,
                        payment_EuroPerSecond = 0.0,
                        fuelPrice_EuroPerLiter = 0.0,
                        vechicleSpeed_MeterPerSecond = 0L,
                        volumeCapacity_Stere = 0L,
                        weightCapacity_Gramm = 0L,
                        workTime_SecondPerDay = 0L,
                    )
                )
            ), DSetting(
                name = "exampleSetting",
                algorithm = "exampleAlgorithm",
                iterLimit = BigDecimal(Long.MAX_VALUE),
                timeLimit_Second = BigDecimal(Long.MAX_VALUE)
            )
        )
    )
    val je: JsonElement = JsonParser.parseString(uglyJsonString)

    file.writeText(gson.toJson(je))
}
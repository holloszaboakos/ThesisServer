package thesis

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import thesis.model.mtsp.*
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
                        lattitude = BigDecimal(0.0),
                        longitude = BigDecimal(0.0),
                    ),
                    objectives = arrayOf(
                        DObjective(
                            name = "exampleObjective",
                            location = DGps(
                                lattitude = BigDecimal(0.0),
                                longitude = BigDecimal(0.0),
                            ),
                            time_Second = BigDecimal(0.0),
                            volume_Stere = BigDecimal(0.0),
                            weight_Gramm = BigDecimal(0.0),
                        )
                    ),
                    edgesToCenter = arrayOf(
                        DEdge(
                            name = "exampleEdges0ToCenter",
                            length_Meter = BigDecimal(0),
                            rout = arrayOf(
                                DGps(
                                    lattitude = BigDecimal(0),
                                    longitude = BigDecimal(0)
                                )
                            )
                        ),
                        DEdge(
                            name = "exampleEdges1ToCenter",
                            length_Meter = BigDecimal(0),
                            rout = arrayOf(
                                DGps(
                                    lattitude = BigDecimal(0),
                                    longitude = BigDecimal(0)
                                )
                            )
                        ),
                    ),
                    edgesFromCenter = arrayOf(
                        DEdge(
                            name = "exampleEdges0FromCenter",
                            length_Meter = BigDecimal(0),
                            rout = arrayOf(
                                DGps(
                                    lattitude = BigDecimal(0),
                                    longitude = BigDecimal(0)
                                )
                            )
                        ),
                        DEdge(
                            name = "exampleEdges1FromCenter",
                            length_Meter = BigDecimal(0),
                            rout = arrayOf(
                                DGps(
                                    lattitude = BigDecimal(0),
                                    longitude = BigDecimal(0)
                                )
                            )
                        ),
                    ),
                    edgesBetween = arrayOf(
                        DEdgeArray(
                            values = arrayOf(
                                DEdge(
                                    name = "exampleEdgesBetween0to1",
                                    length_Meter = BigDecimal(0),
                                    rout = arrayOf(
                                        DGps(
                                            lattitude = BigDecimal(0),
                                            longitude = BigDecimal(0)
                                        )
                                    )
                                ),
                            )
                        ),
                        DEdgeArray(
                            values = arrayOf(
                                DEdge(
                                    name = "exampleEdgesBetween1to0",
                                    length_Meter = BigDecimal(0),
                                    rout = arrayOf(
                                        DGps(
                                            lattitude = BigDecimal(0),
                                            longitude = BigDecimal(0)
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
                        basePrice_Euro = BigDecimal(0),
                        fuelConsuption_LiterPerMeter = BigDecimal(0),
                        payment_EuroPerSecond = BigDecimal(0),
                        fuelPrice_EuroPerLiter = BigDecimal(0),
                        vechicleSpeed_MeterPerSecond = BigDecimal(0),
                        volumeCapacity_Stere = BigDecimal(0),
                        weightCapacity_Gramm = BigDecimal(0),
                        workTime_SecondPerDay = BigDecimal(0),
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
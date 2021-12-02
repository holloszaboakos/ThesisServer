package hu.bme.thesis.logic.common.steps

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.model.mtsp.DEdge
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

enum class ECost {
    NO_CAPACITY {
        override fun <S : ISpecimenRepresentation> invoke(
            alg: AAlgorithm4VRP<S>,
            specimen: ISpecimenRepresentation
        ) {
            alg.run {
                var sumCost = 0.0
                var geneIndex = 0
                specimen.forEachSliceIndexed { sliceIndex, sliceFlow ->
                    val slice = runBlocking { sliceFlow.toList() }
                    val salesman = salesmen[sliceIndex]
                    var cost = salesman.basePrice_Euro
                    slice.map { it }.forEachIndexed { index, value ->
                        when (index) {
                            0 -> {
                                val fromCenterEdge = costGraph.edgesFromCenter[value]
                                val objective = costGraph.objectives[value]
                                cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * fromCenterEdge.length_Meter +
                                        salesman.payment_EuroPerSecond * fromCenterEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                        salesman.payment_EuroPerSecond * objective.time_Second
                            }
                            geneIndex + slice.size - 1 -> {
                                val betweenEdge = if (slice[index - 1] > value)
                                    (costGraph.edgesBetween[slice[index - 1]].values[value])
                                else
                                    (costGraph.edgesBetween[slice[index - 1]].values[value - 1])
                                val objective = costGraph.objectives[value]
                                val toCenterEdge = costGraph.edgesToCenter[value]
                                cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * betweenEdge.length_Meter +
                                        salesman.payment_EuroPerSecond * betweenEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                        salesman.payment_EuroPerSecond * objective.time_Second +
                                        salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * toCenterEdge.length_Meter +
                                        salesman.payment_EuroPerSecond * toCenterEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond

                            }
                            else -> {
                                val betweenEdge = if (slice[index - 1] > value)
                                    costGraph.edgesBetween[slice[index - 1]].values[value]
                                else
                                    try {
                                        costGraph.edgesBetween[slice[index - 1]].values[value - 1]

                                    } catch (e: ArrayIndexOutOfBoundsException) {
                                        println("fuck!")
                                        DEdge()
                                    }

                                val objective = costGraph.objectives[value]
                                cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * betweenEdge.length_Meter +
                                        salesman.payment_EuroPerSecond * betweenEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                        salesman.payment_EuroPerSecond * objective.time_Second
                            }
                        }

                    }
                    geneIndex += slice.size
                    sumCost += cost.toLong()

                }
                specimen.cost = sumCost
                specimen.costCalculated = true
                if (sumCost == 0.0) {
                    println("Impossible!")
                }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        alg: AAlgorithm4VRP<S>,
        specimen: ISpecimenRepresentation
    )
}
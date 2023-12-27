package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.evolutionary.GeneticAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.model.mtsp.DEdge

enum class ECost {
    NO_CAPACITY {
        override fun <P : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<P>, permutation: ISpecimenRepresentation){
                alg.run {
                    var sumCost = 0.0
                    var geneIndex = 0
                    permutation.forEachSliceIndexed { sliceIndex, sliceSequence ->
                        val slice = sliceSequence.toList()
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

                                        }
                                        catch (e:ArrayIndexOutOfBoundsException){
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
                    permutation.cost = sumCost
                    permutation.costCalculated = true
                    if (sumCost == 0.0) {
                        println("Impossible!")
                    }
                }
            }
    };

    abstract operator fun <P : ISpecimenRepresentation> invoke(alg: GeneticAlgorithm<P>, permutation: ISpecimenRepresentation)
}
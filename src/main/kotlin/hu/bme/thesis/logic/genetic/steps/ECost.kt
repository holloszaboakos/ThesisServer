package hu.bme.thesis.logic.genetic.steps

import hu.bme.thesis.logic.genetic.DGeneticAlgorithm
import hu.bme.thesis.logic.permutation.IPermutation
import hu.bme.thesis.model.mtsp.DEdge

enum class ECost {
    NO_CAPACITY {
        override fun invoke(alg: DGeneticAlgorithm<*>, permutation: IPermutation) {
            val objectives = alg.objectives
            val salesmen = alg.salesmen
            val costGraph = alg.costGraph


            var sumCost = 0.0
            var geneIndex = 0
            permutation.forEachSliceIndexed { sliceIndex, slice ->
                val salesman = salesmen[sliceIndex]
                var cost = salesman.basePrice_Euro
                slice.forEachIndexed { index, value ->
                    when (index) {
                        0 -> {
                            val fromCenterEdge = costGraph.edgesFromCenter[value]
                            val objective = objectives[value]
                            cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * fromCenterEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * fromCenterEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                    salesman.payment_EuroPerSecond * objective.time_Second
                        }
                        geneIndex + slice.size - 1 -> {
                            val betweenEdge = if (slice[index - 1] > value)
                                (costGraph.edgesBetween[slice[index - 1]].values[value])
                            else
                                (costGraph.edgesBetween[slice[index - 1]].values[value - 1])
                            val objective = objectives[value]
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
                                    permutation
                                    throw e
                                }

                            val objective = objectives[value]
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
        }
    };

    abstract operator fun invoke(alg: DGeneticAlgorithm<*>, permutation: IPermutation)
}
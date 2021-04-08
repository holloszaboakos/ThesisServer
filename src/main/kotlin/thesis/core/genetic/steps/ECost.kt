package thesis.core.genetic.steps

import thesis.core.permutation.TwoPartRepresentation
import thesis.core.genetic.GeneticAlgorithm
import thesis.core.permutation.IPermutation
import java.math.BigDecimal

enum class ECost {
    NO_CAPACITY {
        override fun invoke(alg: GeneticAlgorithm, permutation: IPermutation) {
            val objectives = alg.objectives
            val salesmen = alg.salesmen
            val costGraph = alg.costGraph


            var sumCost = BigDecimal(0)
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
                            val betweenEdge = if (permutation[index - 1] > value)
                                (costGraph.edgesBetween[permutation[index - 1]].values[value])
                            else
                                (costGraph.edgesBetween[permutation[index - 1]].values[value - 1])
                            val objective = objectives[value]
                            val toCenterEdge = costGraph.edgesToCenter[value]
                            cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * betweenEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * betweenEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                    salesman.payment_EuroPerSecond * objective.time_Second +
                                    salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * toCenterEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * toCenterEdge.length_Meter/ salesman.vechicleSpeed_MeterPerSecond

                        }
                        else -> {
                            val betweenEdge = if (permutation[index - 1] > value)
                                (costGraph.edgesBetween[permutation[index - 1]].values[value])
                            else
                                (costGraph.edgesBetween[permutation[index - 1]].values[value - 1])
                            val objective = objectives[value]
                            cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter* betweenEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * betweenEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                    salesman.payment_EuroPerSecond * objective.time_Second
                        }
                    }

                }
                geneIndex += slice.size
                sumCost += cost
            }
            permutation.cost = sumCost
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm, permutation: IPermutation)
}
package thesis.core.genetic.steps

import thesis.core.Permutation
import thesis.core.genetic.GeneticAlgorithm
import java.math.BigDecimal

enum class ECost {
    NO_CAPACITY {
        override fun invoke(alg: GeneticAlgorithm, permutation: Permutation) {
            val objectives = alg.objectives
            val salesmen = alg.salesmen
            val costGraph = alg.costGraph


            var sumCost = BigDecimal(0)
            var geneIndex = 0
            permutation.sliceLengthes.forEachIndexed { sliceIndex, sliceLength ->
                val salesman = salesmen[sliceIndex]
                var cost = salesman.basePrice_Euro
                (geneIndex until (geneIndex + sliceLength)).forEach { sliceGeneIndex ->
                    when (sliceGeneIndex) {
                        0 -> {
                            val fromCenterEdge = costGraph.edgesFromCenter[permutation[sliceGeneIndex]]
                            val objective = objectives[permutation[sliceGeneIndex]]
                            cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * fromCenterEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * fromCenterEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                    salesman.payment_EuroPerSecond * objective.time_Second
                        }
                        geneIndex + sliceLength - 1 -> {
                            val betweenEdge = if (permutation[sliceGeneIndex - 1] > permutation[sliceGeneIndex])
                                (costGraph.edgesBetween[permutation[sliceGeneIndex - 1]].values[permutation[sliceGeneIndex]])
                            else
                                (costGraph.edgesBetween[permutation[sliceGeneIndex - 1]].values[permutation[sliceGeneIndex] - 1])
                            val objective = objectives[permutation[sliceGeneIndex]]
                            val toCenterEdge = costGraph.edgesToCenter[permutation[sliceGeneIndex]]
                            cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * betweenEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * betweenEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                    salesman.payment_EuroPerSecond * objective.time_Second +
                                    salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * toCenterEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * toCenterEdge.length_Meter/ salesman.vechicleSpeed_MeterPerSecond

                        }
                        else -> {
                            val betweenEdge = if (permutation[sliceGeneIndex - 1] > permutation[sliceGeneIndex])
                                (costGraph.edgesBetween[permutation[sliceGeneIndex - 1]].values[permutation[sliceGeneIndex]])
                            else
                                (costGraph.edgesBetween[permutation[sliceGeneIndex - 1]].values[permutation[sliceGeneIndex] - 1])
                            val objective = objectives[permutation[sliceGeneIndex]]
                            cost += salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter* betweenEdge.length_Meter +
                                    salesman.payment_EuroPerSecond * betweenEdge.length_Meter / salesman.vechicleSpeed_MeterPerSecond +
                                    salesman.payment_EuroPerSecond * objective.time_Second
                        }
                    }

                }
                geneIndex += sliceLength
                sumCost += cost
            }
            permutation.cost = sumCost
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm, permutation: Permutation)
}
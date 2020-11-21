package thesis.core.genetic.steps

import thesis.core.Permutation
import thesis.core.genetic.GeneticAlgorithm

enum class ECost {
    NO_CAPACITY {
        override fun invoke(alg: GeneticAlgorithm, permutation: Permutation) {
            val objectives = alg.objectives
            val salesmen = alg.salesmen
            val costGraph = alg.costGraph


            var geneIndex = 0
            (permutation.sliceLengthes.indices).forEachIndexed { sliceIndex, sliceLength ->
                val salesman = salesmen[sliceIndex]
                var cost = salesman.basePrice_Euro
                (geneIndex until sliceLength).forEach { sliceGeneIndex ->
                    if (sliceGeneIndex == 0) {
                        val fromCenterEdge = costGraph.edgesFromCenter[permutation[geneIndex + sliceGeneIndex]]
                        val objective = objectives[permutation[geneIndex + sliceGeneIndex]]
                        cost += (salesman.fuelPrice_EuroPerLiter
                                * salesman.fuelConsuption_LiterPerMeter
                                * fromCenterEdge.length_Meter
                                ) + (salesman.payment_EuroPerSecond
                                * (fromCenterEdge.length_Meter
                                / salesman.vechicleSpeed_MeterPerSecond)
                                )
                        cost += objective.time_Second
                    } else if (sliceGeneIndex == sliceLength - 1) {
                        val betweenEdge = (costGraph.edgesBetween
                                [permutation[geneIndex + sliceGeneIndex - 1]]
                                [permutation[geneIndex + sliceGeneIndex]]
                                )
                        val objective = objectives[permutation[geneIndex + sliceGeneIndex]]
                        val toCenterEdge = costGraph.edgesToCenter[permutation[geneIndex + sliceGeneIndex]]
                        cost += (salesman.fuelPrice_EuroPerLiter
                                * salesman.fuelConsuption_LiterPerMeter
                                * betweenEdge.length_Meter
                                ) + (salesman.payment_EuroPerSecond
                                * (betweenEdge.length_Meter
                                / salesman.vechicleSpeed_MeterPerSecond)
                                )
                        cost += objective.time_Second
                        cost += (salesman.fuelPrice_EuroPerLiter
                                * salesman.fuelConsuption_LiterPerMeter
                                * toCenterEdge.length_Meter
                                ) + (salesman.payment_EuroPerSecond
                                * (toCenterEdge.length_Meter
                                / salesman.vechicleSpeed_MeterPerSecond)
                                )
                    } else {
                        val betweenEdge = (costGraph.edgesBetween
                                [permutation[geneIndex + sliceGeneIndex - 1]]
                                [permutation[geneIndex + sliceGeneIndex]]
                                )
                        val objective = objectives[permutation[geneIndex + sliceGeneIndex]]
                        cost += (salesman.fuelPrice_EuroPerLiter
                                * salesman.fuelConsuption_LiterPerMeter
                                * betweenEdge.length_Meter
                                ) + (salesman.payment_EuroPerSecond
                                * (betweenEdge.length_Meter
                                / salesman.vechicleSpeed_MeterPerSecond)
                                )
                        cost += objective.time_Second
                    }

                }
                geneIndex += sliceLength
            }
        }
    };

    abstract operator fun invoke(alg: GeneticAlgorithm, permutation: Permutation)
}
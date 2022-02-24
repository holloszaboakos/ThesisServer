package hu.bme.thesis.logic.linkernighan

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.mtsp.*
import kotlinx.coroutines.runBlocking
import java.util.*


//usage:
// 1. instantiate,
// 2. runAlgorithm
class LinKernighanTSP<S : ISpecimenRepresentation>(
    //The instance variables definitions
    var permutationFactory: SSpecimenRepresentationFactory<S>,
    private val coordinates: Array<DGps>, //The instance variables definitions
    private val size: Int,
    private val salesmen: Array<DSalesman>,
    private val costGraph: DGraph
) {

    companion object {
        fun costOfEdge(edge: DEdge, salesman: DSalesman) =
            salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * edge.length_Meter +
                    salesman.payment_EuroPerSecond * edge.length_Meter / salesman.vechicleSpeed_MeterPerSecond

        fun costOfObjective(objective: DObjective, salesman: DSalesman) =
            salesman.payment_EuroPerSecond * objective.time_Second
    }

    fun calcCost(specimen: S) {
        var sumCost = 0.0
        var geneIndex = 0
        runBlocking {
            specimen.forEachSliceIndexed { sliceIndex, sliceSequence ->
                val slice = runBlocking { sliceSequence.toList() }
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
        }
        specimen.cost = sumCost
        specimen.costCalculated = true
        if (sumCost == 0.0) {
            println("Impossible!")
        }
    }

    /**
     * This function returns the current tour distance
     * @param Nothing
     * @return double the distance of the tour
     */
    val result: S
        get() {
            return runBlocking {
                val specimen = permutationFactory.produce(arrayOf(tour))
                calcCost(specimen)
                specimen
            }
        }

    // The current tour solution
    var tour: IntArray = IntArray(size) { it }

    init {
        tour.shuffle()
    }
}
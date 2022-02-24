package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.inner.setup.DNearestNeighbourSetup
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

//No Objective cost: cost of greedy: 6.1089406835078902E18
//Objective cost: cost of greedy: 3.1999213104095421E18
class NearestNeighbourMTSPOneEach<S : ISpecimenRepresentation>(
    override var subSolutionFactory: SSpecimenRepresentationFactory<S>,
    override var costGraph: DGraph,
    override var salesmen: Array<DSalesman>,
    override var setup: DNearestNeighbourSetup,
    override var timeLimit: Long = 0L,
) : SNearestNeighbour<S>(subSolutionFactory, costGraph, salesmen, setup, timeLimit) {
    override suspend fun run(): S {
        val tours = Array(salesmen.size) { mutableListOf<Int>() }
        val costs = DoubleArray(salesmen.size) { 0.0 }
        val contains = BooleanArray(costGraph.objectives.size) { false }
        costGraph.objectives.indices.forEach { geneIndex ->
            val tourIndex = geneIndex % salesmen.size

            if (tours[tourIndex].isEmpty()) {
                val customerIndex = costGraph.objectives.indices
                    .filter { !contains[it] }
                    .minByOrNull {
                        costOfEdge(costGraph.edgesFromCenter[it], salesmen[tourIndex]) +
                        costOfObjective(costGraph.objectives[it], salesmen[tourIndex])
                    } ?: -1
                tours[tourIndex].add(customerIndex)
                contains[customerIndex] = true
                costs[customerIndex] += costOfEdge(costGraph.edgesFromCenter[customerIndex], salesmen[tourIndex]) +
                        costOfObjective(costGraph.objectives[customerIndex], salesmen[tourIndex])
            } else {
                val previousIndex = tours[tourIndex].last()
                val customerIndex = costGraph.objectives.indices
                    .filter { !contains[it] }
                    .minByOrNull {
                        costs[tourIndex] +
                                costOfEdge(
                                    costGraph
                                        .edgesBetween[previousIndex]
                                        .values[it - if (previousIndex > it) 0 else 1],
                                    salesmen[tourIndex]
                                ) + costOfObjective(costGraph.objectives[it], salesmen[tourIndex])
                    } ?: -1
                tours[tourIndex].add(customerIndex)
                contains[customerIndex] = true
                costs[customerIndex] +=
                    costOfEdge(
                        costGraph
                            .edgesBetween[previousIndex]
                            .values[customerIndex - if (previousIndex > customerIndex) 0 else 1],
                        salesmen[tourIndex]
                    ) + costOfObjective(costGraph.objectives[customerIndex], salesmen[tourIndex])
            }

        }
        val resultSpecimen = subSolutionFactory.produce(tours.map { it.toIntArray() }.toTypedArray())
        cost(resultSpecimen)
        return resultSpecimen
    }
}
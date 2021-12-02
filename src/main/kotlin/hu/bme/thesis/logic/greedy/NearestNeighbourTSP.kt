package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.inner.setup.DNearestNeighbourSetup
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

//no toCenter, no Objective, cost: cost of greedy: 187076.0
//no toCenter, Objective, cost: cost of greedy: 186222.0
class NearestNeighbourTSP<S : ISpecimenRepresentation>(
    override var permutationFactory: SSpecimenRepresentationFactory<S>,
    override var costGraph: DGraph,
    override var salesmen: Array<DSalesman>,
    override var setup: DNearestNeighbourSetup,
    override var timeLimit: Long = 0L,
    override var iterationLimit: Int = 0,
) : SNearestNeighbour<S>(permutationFactory, costGraph, salesmen, setup, timeLimit, iterationLimit) {
    override suspend fun run(): S {
        val startIndex = (0 until costGraph.objectives.size)
            .minByOrNull {
                costOfEdge(costGraph.edgesFromCenter[it], salesmen[0])//+
                        //costOfObjective(costGraph.objectives[it], salesmen[0])
            }
            ?: -1

        val contains = BooleanArray(costGraph.objectives.size) { false }
        val resultPermutation = IntArray(costGraph.objectives.size) { -1 }
        resultPermutation[0] = startIndex
        contains[resultPermutation[0]] = true

        (1 until costGraph.objectives.size).forEach { geneIndex ->
            val previousIndex = resultPermutation[geneIndex - 1]
            resultPermutation[geneIndex] = (0 until costGraph.objectives.size)
                .filter { !contains[it] }
                .minByOrNull {
                    costOfEdge(
                        costGraph
                            .edgesBetween[previousIndex]
                            .values[it - if (previousIndex > it) 0 else 1],
                        salesmen[0]
                    ) //+
                    //costOfObjective(costGraph.objectives[it], salesmen[0])
                } ?: -1
            contains[resultPermutation[geneIndex]] = true
        }
        val resultSpecimen = permutationFactory.produce(arrayOf(resultPermutation))
        cost(resultSpecimen)
        return resultSpecimen
    }
}
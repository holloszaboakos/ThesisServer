package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

class NearestNeighbourTSP2MTSP<S : ISpecimenRepresentation>(
    permutationFactory: SSpecimenRepresentationFactory<S>,
    costGraph: DGraph,
    salesmen: Array<DSalesman>
) : SNearestNeighbour<S>(permutationFactory, costGraph, salesmen) {
    override fun run(): S {
        var bestSpecimen : S? = null
        for(permutationIndex in costGraph.objectives.indices){
            val contains = BooleanArray(costGraph.objectives.size) { false }
            val resultPermutation = IntArray(costGraph.objectives.size) { -1 }
            resultPermutation[0] = permutationIndex
            contains[permutationIndex] = true

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
            calcCost(resultSpecimen)
            if(bestSpecimen == null || bestSpecimen.cost > resultSpecimen.cost)
                bestSpecimen = resultSpecimen
        }

        bestSpecimen?.let {
            val data = it.getData()[0]
            val newData = mutableListOf<IntArray>()


        } ?: throw NullPointerException("bestSpecimen should not be null!")
        TODO()
    }
}
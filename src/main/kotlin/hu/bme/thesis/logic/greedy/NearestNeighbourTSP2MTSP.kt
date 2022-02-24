package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.inner.setup.DNearestNeighbourSetup
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman
import kotlinx.coroutines.flow.toList

class NearestNeighbourTSP2MTSP<S : ISpecimenRepresentation>(
    override var subSolutionFactory: SSpecimenRepresentationFactory<S>,
    override var costGraph: DGraph,
    override var salesmen: Array<DSalesman>,
    override var setup: DNearestNeighbourSetup,
    override var timeLimit: Long = 0L,
) : SNearestNeighbour<S>(subSolutionFactory, costGraph, salesmen, setup, timeLimit) {
    override suspend fun run(): S {
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

            val resultSpecimen = subSolutionFactory.produce(arrayOf(resultPermutation))
            cost(resultSpecimen)
            if(bestSpecimen == null || bestSpecimen.cost > resultSpecimen.cost)
                bestSpecimen = resultSpecimen
        }

        bestSpecimen?.let {
            val data = it.getData().toList()[0]
            val newData = mutableListOf<IntArray>()


        } ?: throw NullPointerException("bestSpecimen should not be null!")
        TODO()
    }
}
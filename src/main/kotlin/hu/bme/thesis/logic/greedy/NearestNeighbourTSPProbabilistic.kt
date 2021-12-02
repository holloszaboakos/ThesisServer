package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.inner.setup.DNearestNeighbourSetup
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman
import kotlin.random.Random

//Objective, linear probability: 1820495.0
//Objective, quadratic probability: 641776.0
//Objective, 3 probability: 326567.0
//Objective, 4 probability: 241746.0
//Objective, 8 probability: 178594.0
//Objective, 16 probability: 168439.0
//Objective, 32 probability: 168190.0
//Objective, 64 probability: 166245.0
//Objective, 128 probability: 560772.0
class NearestNeighbourTSPProbabilistic<S : ISpecimenRepresentation>(
    override var permutationFactory: SSpecimenRepresentationFactory<S>,
    override var costGraph: DGraph,
    override var salesmen: Array<DSalesman>,
    override var setup: DNearestNeighbourSetup,
    override var timeLimit: Long = 0L,
    override var iterationLimit: Int = 0,
) : SNearestNeighbour<S>(permutationFactory, costGraph, salesmen, setup, timeLimit, iterationLimit) {

    override suspend fun run(): S {
        var resultSpecimen: S? = null
        costGraph.edgesBetween.maxOf {
            it.values.maxOf { edge ->
                costOfEdge(edge, salesmen[0])
            }
        }
        (1 until costGraph.objectives.size).forEach { iteration ->
            val startIndex = costGraph.objectives.indices
                .minByOrNull {
                    costOfEdge(costGraph.edgesFromCenter[it], salesmen[0]) +
                            costOfObjective(costGraph.objectives[it], salesmen[0])
                }
                ?: -1

            val contains = BooleanArray(costGraph.objectives.size) { false }
            val resultPermutation = IntArray(costGraph.objectives.size) { -1 }

            resultPermutation[0] = startIndex
            contains[resultPermutation[0]] = true

            (1 until costGraph.objectives.size).forEach { geneIndex ->
                val previousIndex = resultPermutation[geneIndex - 1]
                val notUsedYet = (0 until costGraph.objectives.size)
                    .filter { !contains[it] }

                val weights = notUsedYet.map {
                    (1.0 / (
                            costOfEdge(
                                costGraph
                                    .edgesBetween[previousIndex]
                                    .values[it - if (previousIndex > it) 0 else 1],
                                salesmen[0]
                            ) + costOfObjective(costGraph.objectives[it], salesmen[0])
                            ))
                        .let { num -> num * num }
                        .let { num -> num * num }
                        .let { num -> num * num }
                        .let { num -> num * num }
                        .let { num -> num * num }
                        .let { num -> num * num }
                        .let { num -> num * num }
                }
                val sum = weights.sum()

                var random =
                    if (sum <= 0) 0.0
                    else Random.nextDouble(sum)
                for (index in weights.indices) {
                    val value = weights[index]
                    random -= value
                    if (random <= 0) {
                        resultPermutation[geneIndex] = notUsedYet[index]
                        contains[resultPermutation[geneIndex]] = true
                        break
                    }
                }
            }
            val subResultSpecimen = permutationFactory.produce(arrayOf(resultPermutation))
            cost(subResultSpecimen)
            if (resultSpecimen == null || (resultSpecimen?.cost ?: 0.0) > subResultSpecimen.cost) {
                resultSpecimen = subResultSpecimen
                println("$iteration out of ${costGraph.objectives.size} cost: ${resultSpecimen?.cost}")
            }
        }
        return resultSpecimen ?: throw  Error("No result")
    }
}
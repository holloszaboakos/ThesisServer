package hu.bme.thesis.logic.evolutionary

import hu.bme.thesis.logic.evolutionary.setup.GeneticAlgorithmSetup
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

class GeneticAlgorithm<S : ISpecimenRepresentation>(
    permutationFactory: SSpecimenRepresentationFactory<S>,
    timeLimit: Long = 0L,
    iterationLimit: Int = 0,
    costGraph: DGraph,
    salesmen: Array<DSalesman>,
    override val setup: GeneticAlgorithmSetup
) : SEvolutionaryAlgorithm<S>(permutationFactory, timeLimit, iterationLimit, costGraph, salesmen, setup,4 * (costGraph.objectives.size + salesmen.size)){

    fun selection() = setup.selection(this)
    fun crossover() = setup.crossover(this)
    fun crossoverOperator(
        parents: Pair<S, S>,
        child: S
    ) = setup.crossoverOperator(parents, child, this)

    fun mutate() = setup.mutate(this)
}
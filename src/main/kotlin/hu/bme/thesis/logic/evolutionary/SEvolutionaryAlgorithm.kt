package hu.bme.thesis.logic.evolutionary

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.evolutionary.setup.GeneticAlgorithmSetup
import hu.bme.thesis.logic.evolutionary.setup.SEvolutionaryAlgorithmSetup
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

sealed class SEvolutionaryAlgorithm<S : ISpecimenRepresentation>(
    permutationFactory: SSpecimenRepresentationFactory<S>,
    timeLimit: Long = 0L,
    val iterationLimit: Int = 0,
    costGraph: DGraph,
    salesmen: Array<DSalesman>,
    override val setup: SEvolutionaryAlgorithmSetup,
    sizeOfPopulation: Int
) : AAlgorithm4VRP<S>(permutationFactory, costGraph, salesmen, setup, timeLimit) {

    var iteration = 0
    var population: ArrayList<S> = if (costGraph.objectives.size != 1)
        ArrayList(List(sizeOfPopulation) {
            permutationFactory.produce(
                Array(salesmen.size) { index ->
                    if (index == 0)
                        IntArray(costGraph.objectives.size) { it }
                    else
                        intArrayOf()
                }
            )
        })
    else arrayListOf(
        permutationFactory.produce(
            arrayOf(IntArray(costGraph.objectives.size) { it })
        )
    )
    var best: S? = null
    var worst: S? = null

    override suspend fun run() = setup.run(this)
    fun cycle() = setup.cycle(this)
    fun iterate(manageLifeCycle: Boolean) = setup.iteration(this, manageLifeCycle)

    fun initializePopulation() = setup.initializePopulation(this)
    fun orderByCost() = setup.orderByCost(this)
    fun boost() = setup.boost(this)
}
package hu.bme.thesis.logic.evolutionary

import hu.bme.thesis.logic.evolutionary.setup.BacterialAlgorithmSetup
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

class BacterialAlgorithm<S : ISpecimenRepresentation>(
    permutationFactory: SSpecimenRepresentationFactory<S>,
    timeLimit: Long = 0L,
    iterationLimit: Int = 0,
    costGraph: DGraph,
    salesmen: Array<DSalesman>,
    override val setup: BacterialAlgorithmSetup
) : SEvolutionaryAlgorithm<S>(
    permutationFactory,
    timeLimit,
    iterationLimit,
    costGraph,
    salesmen,
    setup,
    100
) {

    //val generationLimit = 10000
    val geneCount = population.first().permutationIndices.count()
    //val populationSize = 100
    //val mutationCount = 10
    val cloneCount = 7
    val cloneSegmentLength = 100
    val cloneCycleCount = 18 //gene count / clone segment length
    val geneTransferSegmentLength= 10
    val injectionCount = 40 //40% of ps

    fun mutate() = setup.mutate( this)

    fun geneTransfer() = setup.geneTransfer(this)
    fun geneTransferOperator(from: S, to: S) =
        setup.geneTransferOperator(this, from, to)
}
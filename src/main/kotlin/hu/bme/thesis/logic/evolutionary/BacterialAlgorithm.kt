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

    fun mutate() = setup.mutate(
        this,
        7,
        100
    )

    fun mutateSpecimen(specimen: S, cloneCount: Int, segmentSize: Int) =
        setup.mutateSpecimen(this, specimen, cloneCount, segmentSize)

    fun produceMutant(specimen: S, segmentPosition: Int, segmentSize: Int) =
        setup.produceMutant(this, specimen, segmentPosition, segmentSize)

    fun geneTransfer() = setup.geneTransfer(this,10)
    fun geneTransferOperator(from: S, to: S, segmentSize: Int) =
        setup.geneTransferOperator(this, from, to, segmentSize)
}
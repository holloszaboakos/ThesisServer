package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.inner.setup.DNearestNeighbourSetup
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

sealed class SNearestNeighbour<S : ISpecimenRepresentation>(
    override var subSolutionFactory: SSpecimenRepresentationFactory<S>,
    override var costGraph: DGraph,
    override var salesmen: Array<DSalesman>,
    override var setup: DNearestNeighbourSetup,
    override var timeLimit: Long = 0L,
) : AAlgorithm4VRP<S>(subSolutionFactory, costGraph, salesmen, setup, timeLimit)
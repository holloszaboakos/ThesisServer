package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.inner.setup.DNearestNeighbourSetup
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

class NearestNeighbourMTSPMarker<S : ISpecimenRepresentation>(
    override var subSolutionFactory: SSpecimenRepresentationFactory<S>,
    override var costGraph: DGraph,
    override var salesmen: Array<DSalesman>,
    override var setup: DNearestNeighbourSetup,
    override var timeLimit: Long = 0L,
) : SNearestNeighbour<S>(subSolutionFactory, costGraph, salesmen, setup, timeLimit) {
    override suspend fun run(): S {
        val tours = Array(salesmen.size) { mutableListOf<Int>() }
        //val costs = DoubleArray(salesmen.size) { 0.0 }
        //val contains = BooleanArray(costGraph.objectives.size) { false }
        //TODO("Not yet implemented")
        val resultSpecimen = subSolutionFactory.produce(tours.map { it.toIntArray() }.toTypedArray())
        cost(resultSpecimen)
        return resultSpecimen
    }
}
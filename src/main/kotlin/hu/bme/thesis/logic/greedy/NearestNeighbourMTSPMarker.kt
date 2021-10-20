package hu.bme.thesis.logic.greedy

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import hu.bme.thesis.logic.specimen.factory.SSpecimenRepresentationFactory
import hu.bme.thesis.model.mtsp.DGraph
import hu.bme.thesis.model.mtsp.DSalesman

class NearestNeighbourMTSPMarker<S : ISpecimenRepresentation>(
    permutationFactory: SSpecimenRepresentationFactory<S>,
    costGraph: DGraph,
    salesmen: Array<DSalesman>
) : SNearestNeighbour<S>(permutationFactory, costGraph, salesmen) {
    override fun run(): S {
        val tours = Array(salesmen.size) { mutableListOf<Int>() }
        val costs = DoubleArray(salesmen.size) { 0.0 }
        val contains = BooleanArray(costGraph.objectives.size) { false }
        //TODO("Not yet implemented")
        val resultSpecimen = permutationFactory.produce(tours.map { it.toIntArray() }.toTypedArray())
        calcCost(resultSpecimen)
        return resultSpecimen
    }
}
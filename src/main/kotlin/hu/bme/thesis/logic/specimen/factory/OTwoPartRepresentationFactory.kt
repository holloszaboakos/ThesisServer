package hu.bme.thesis.logic.specimen.factory

import hu.bme.thesis.logic.specimen.DTwoPartRepresentation

object OTwoPartRepresentationFactory : SPermutationFactory<DTwoPartRepresentation>() {
    override fun produce(values: Array<IntArray>): DTwoPartRepresentation = DTwoPartRepresentation(values)
    override fun copy(permutation: DTwoPartRepresentation) = DTwoPartRepresentation(permutation)
}
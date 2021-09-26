package hu.bme.thesis.logic.specimen.factory

import hu.bme.thesis.logic.specimen.DOnePartRepresentation

object OOnePartRepresentationFactory : SPermutationFactory<DOnePartRepresentation>() {
    override fun produce(values: Array<IntArray>): DOnePartRepresentation = DOnePartRepresentation(values)
    override fun copy(permutation: DOnePartRepresentation) = DOnePartRepresentation(permutation)
}
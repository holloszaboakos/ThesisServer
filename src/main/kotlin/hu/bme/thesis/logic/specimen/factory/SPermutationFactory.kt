package hu.bme.thesis.logic.specimen.factory

import hu.bme.thesis.logic.specimen.IRepresentation

sealed class SPermutationFactory<P : IRepresentation> {
    abstract fun produce(values: Array<IntArray>) : P
    abstract fun copy(permutation : P) : P
}
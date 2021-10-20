package hu.bme.thesis.logic.specimen.factory

import hu.bme.thesis.logic.specimen.ISpecimenRepresentation

sealed class SSpecimenRepresentationFactory<S : ISpecimenRepresentation> {
    abstract fun produce(values: Array<IntArray>) : S
    abstract fun copy(specimen : S) : S
}
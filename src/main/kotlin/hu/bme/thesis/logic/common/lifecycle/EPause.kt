package hu.bme.thesis.logic.common.lifecycle

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation

enum class EPause {
    DEFAULT {
        override suspend operator fun <S : ISpecimenRepresentation> invoke(alg: AAlgorithm4VRP<S>) {
            when (alg.state) {
                AAlgorithm4VRP.State.RESUMED -> {
                    alg.state = AAlgorithm4VRP.State.INITIALIZED
                    alg.job?.join()
                    alg.job = null
                }
                else -> {
                }
            }
        }
    };

    abstract suspend operator fun <S : ISpecimenRepresentation>invoke(alg: AAlgorithm4VRP<S>)
}
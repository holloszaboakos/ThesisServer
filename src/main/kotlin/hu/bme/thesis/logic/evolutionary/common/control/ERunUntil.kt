package hu.bme.thesis.logic.evolutionary.common.control

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.evolutionary.SEvolutionaryAlgorithm
import kotlinx.coroutines.runBlocking
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation

enum class ERunUntil {
    DEFAULT {
        override fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>):S {
            alg.run {
                state = AAlgorithm4VRP.State.RESUMED
                while (
                    runTimeInSeconds < timeLimit
                    && iteration < iterationLimit
                    && state == AAlgorithm4VRP.State.RESUMED
                ) runBlocking {
                    iterate(false)
                }
                state = AAlgorithm4VRP.State.INITIALIZED
                return best ?: throw Error("No result!")
            }
        }
    },;

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: SEvolutionaryAlgorithm<S>) :S
}
package hu.bme.thesis.logic.common.lifecycle

import hu.bme.thesis.logic.common.AAlgorithm4VRP
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

enum class EResume {
    DEFAULT {
        override suspend fun <S : ISpecimenRepresentation>invoke(alg: AAlgorithm4VRP<S>): Unit = coroutineScope {
            when (alg.state) {
                AAlgorithm4VRP.State.CREATED -> {
                    alg.initialize()
                    alg.resume()
                }
                AAlgorithm4VRP.State.INITIALIZED -> {
                    alg.job = launch(Dispatchers.Default){
                        alg.state = AAlgorithm4VRP.State.RESUMED
                        alg.run()
                    }
                }
                else -> {
                }
            }
        }
    };

    abstract suspend operator  fun <S : ISpecimenRepresentation>invoke(alg: AAlgorithm4VRP<S>)
}
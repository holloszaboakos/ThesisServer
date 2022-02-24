package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class EGeneTransfer {
    FROM_BETTER_TO_WORSE{
        override fun <S : ISpecimenRepresentation> invoke(alg: BacterialAlgorithm<S>, segmentSize:Int) {
            alg.run {
                runBlocking {
                    val worse = population.slice(population.size / 2 until population.size)

                    for (index in 0 until population.size / 2)
                        launch(Dispatchers.Default) {
                            geneTransferOperator(population[index], worse[index], segmentSize)
                        }
                }
            }
        }
    };
    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: BacterialAlgorithm<S>, segmentSize:Int)
}
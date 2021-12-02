package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.coroutineScope

enum class EGeneTransfer {
    FROM_BETTER_TO_WORSE{
        override fun <S : ISpecimenRepresentation> invoke(alg: BacterialAlgorithm<S>, segmentSize:Int) {
            alg.run{
                for(index in 0 until population.size / 2){
                    geneTransferOperator(population[index],population[population.size-1-index],segmentSize)
                }
            }
        }
    };
    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: BacterialAlgorithm<S>, segmentSize:Int)
}
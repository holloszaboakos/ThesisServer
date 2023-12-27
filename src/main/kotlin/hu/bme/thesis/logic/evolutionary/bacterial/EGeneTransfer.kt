package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

enum class EGeneTransfer {

    TOURNAMENT{
        override fun <S : ISpecimenRepresentation> invoke(alg: BacterialAlgorithm<S>) {
            alg.run {
                runBlocking {

                    repeat(injectionCount){
                        val r1 = Random.nextInt(alg.population.size - 1)
                        var r2 = Random.nextInt(alg.population.size)
                        if(r1 == r2){
                            r2++
                            val specimen = listOf(population[r1], population[r2])
                            val better = specimen.minByOrNull { it.cost } ?: specimen[0]
                            val worse = specimen.maxByOrNull { it.cost } ?: specimen[1]
                            if(better.cost >= worse.cost){
                                println("LOL")
                            }
                            geneTransferOperator( better, worse )

                            cost(worse)
                        }

                    }
                }
            }
        }
    },

    FROM_BETTER_TO_WORSE{
        override fun <S : ISpecimenRepresentation> invoke(alg: BacterialAlgorithm<S>) {

            alg.run {
                runBlocking {
                    val worse = population.slice(population.size / 2 until population.size)

                    for (index in 0 until population.size / 2)
                        launch(Dispatchers.Default) {
                            geneTransferOperator(population[index], worse[index])
                        }
                }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(alg: BacterialAlgorithm<S>)
}

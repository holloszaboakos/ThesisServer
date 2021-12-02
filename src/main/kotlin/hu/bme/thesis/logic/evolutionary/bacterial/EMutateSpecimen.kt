package hu.bme.thesis.logic.evolutionary.bacterial

import hu.bme.thesis.logic.evolutionary.BacterialAlgorithm
import hu.bme.thesis.logic.specimen.ISpecimenRepresentation
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.random.nextInt

enum class EMutateSpecimen {
    KEEP_ORIGINAL {
        override fun <S : ISpecimenRepresentation> invoke(
            alg: BacterialAlgorithm<S>,
            specimen: S,
            cloneCount: Int,
            segmentSize: Int
        ) {
            alg.run {
                val randomPosition = Random.nextInt(0 until (specimen.permutationSize - segmentSize))

                val clones = MutableList(cloneCount + 1) {
                    if (it == 0) specimen else produceMutant(
                        specimen,
                        randomPosition,
                        segmentSize
                    )
                }
                clones.onEach { runBlocking { cost(it) } }.sortBy { it.cost }
                runBlocking {
                    specimen.setData(
                        if (Random.nextInt(15) != 0 || alg.population[0] == specimen)
                            clones[0].getData()
                        else clones[1].getData()
                    )
                }
            }
        }
    };

    abstract operator fun <S : ISpecimenRepresentation> invoke(
        alg: BacterialAlgorithm<S>,
        specimen: S,
        cloneCount: Int,
        segmentSize: Int
    )
}
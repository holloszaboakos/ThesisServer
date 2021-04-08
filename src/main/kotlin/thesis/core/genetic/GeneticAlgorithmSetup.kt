package thesis.core.genetic

import thesis.core.genetic.control.ECycle
import thesis.core.genetic.control.EIteration
import thesis.core.genetic.control.ERunUntil
import thesis.core.genetic.lifecycle.EPause
import thesis.core.genetic.lifecycle.EResume
import thesis.core.genetic.lifecycle.EInitialize
import thesis.core.genetic.lifecycle.EClear
import thesis.core.genetic.steps.*
import thesis.core.permutation.EPermutationFactory

data class GeneticAlgorithmSetup (
    val permutationFactory:EPermutationFactory,

    val pause:EPause,
    val resume:EResume,
    val initialize:EInitialize,
    val clear:EClear,

    val run: ERunUntil,
    val cycle: ECycle,
    val iteration: EIteration,

    val initializePopulation:EInicializePopulation,
    val cost: ECost,
    val orderByCost: EOrderPopulationByCost,
    val boost: EBoost,
    val selection: ESelectSurvivers,
    val crossover:ECrossOvers,
    val crossoverOperator:ECrossOverOperator,
    val mutate:EMutateChildren
)
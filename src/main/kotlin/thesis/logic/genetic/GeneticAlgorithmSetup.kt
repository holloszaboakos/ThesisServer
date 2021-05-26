package thesis.logic.genetic

import thesis.logic.genetic.control.ECycle
import thesis.logic.genetic.control.EIteration
import thesis.logic.genetic.control.ERunUntil
import thesis.logic.genetic.lifecycle.EPause
import thesis.logic.genetic.lifecycle.EResume
import thesis.logic.genetic.lifecycle.EInitialize
import thesis.logic.genetic.lifecycle.EClear
import thesis.logic.genetic.steps.*

data class GeneticAlgorithmSetup (
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
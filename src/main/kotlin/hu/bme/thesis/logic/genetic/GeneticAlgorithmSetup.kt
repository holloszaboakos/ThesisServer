package hu.bme.thesis.logic.genetic

import hu.bme.thesis.logic.genetic.control.ECycle
import hu.bme.thesis.logic.genetic.control.EIteration
import hu.bme.thesis.logic.genetic.control.ERunUntil
import hu.bme.thesis.logic.genetic.lifecycle.EPause
import hu.bme.thesis.logic.genetic.lifecycle.EResume
import hu.bme.thesis.logic.genetic.lifecycle.EInitialize
import hu.bme.thesis.logic.genetic.lifecycle.EClear
import hu.bme.thesis.logic.genetic.steps.*

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
    val selection: ESelectSurvivors,
    val crossover:ECrossOvers,
    val crossoverOperator:ECrossOverOperator,
    val mutate:EMutateChildren
)
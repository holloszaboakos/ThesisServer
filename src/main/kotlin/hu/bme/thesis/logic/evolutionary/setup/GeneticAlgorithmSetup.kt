package hu.bme.thesis.logic.evolutionary.setup

import hu.bme.thesis.logic.common.steps.ECost
import hu.bme.thesis.logic.evolutionary.common.control.ECycle
import hu.bme.thesis.logic.evolutionary.common.control.EIteration
import hu.bme.thesis.logic.evolutionary.common.control.ERunUntil
import hu.bme.thesis.logic.common.lifecycle.EPause
import hu.bme.thesis.logic.common.lifecycle.EResume
import hu.bme.thesis.logic.common.lifecycle.EInitialize
import hu.bme.thesis.logic.common.lifecycle.EClear
import hu.bme.thesis.logic.common.steps.ECostOfEdge
import hu.bme.thesis.logic.common.steps.ECostOfObjective
import hu.bme.thesis.logic.evolutionary.common.EBoost
import hu.bme.thesis.logic.evolutionary.common.EInicializePopulation
import hu.bme.thesis.logic.evolutionary.common.EOrderPopulationByCost
import hu.bme.thesis.logic.evolutionary.genetic.*

data class GeneticAlgorithmSetup(
    override val pause: EPause,
    override val resume: EResume,
    override val initialize: EInitialize,
    override val clear: EClear,

    override val run: ERunUntil,
    override val cycle: ECycle,
    override val iteration: EIteration,

    override val cost: ECost,
    override val costOfEdge: ECostOfEdge,
    override val costOfObjective: ECostOfObjective,

    override val initializePopulation: EInicializePopulation,
    override val orderByCost: EOrderPopulationByCost,
    override val boost: EBoost,
    val selection: ESelectSurvivors,
    val crossover: ECrossOvers,
    val crossoverOperator: ECrossOverOperator,
    val mutate: EMutateChildren
) : SEvolutionaryAlgorithmSetup(
    pause,
    resume,
    initialize,
    clear,
    run,
    cycle,
    iteration,
    cost,
    costOfEdge,
    costOfObjective,
    initializePopulation,
    orderByCost,
    boost
)
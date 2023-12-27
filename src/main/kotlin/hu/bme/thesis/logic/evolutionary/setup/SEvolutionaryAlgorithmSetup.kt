package hu.bme.thesis.logic.evolutionary.setup

import hu.bme.thesis.logic.common.lifecycle.EClear
import hu.bme.thesis.logic.common.lifecycle.EInitialize
import hu.bme.thesis.logic.common.lifecycle.EPause
import hu.bme.thesis.logic.common.lifecycle.EResume
import hu.bme.thesis.logic.common.steps.ECost
import hu.bme.thesis.logic.common.steps.ECostOfEdge
import hu.bme.thesis.logic.common.steps.ECostOfObjective
import hu.bme.thesis.logic.evolutionary.common.EInicializePopulation
import hu.bme.thesis.logic.evolutionary.common.EOrderPopulationByCost
import hu.bme.thesis.logic.evolutionary.common.control.ECycle
import hu.bme.thesis.logic.evolutionary.common.control.EIteration
import hu.bme.thesis.logic.evolutionary.common.control.ERunUntil
import hu.bme.thesis.logic.evolutionary.common.EBoost
import hu.bme.thesis.model.inner.setup.AAlgorithm4VRPSetup

sealed class SEvolutionaryAlgorithmSetup (
    override val pause: EPause,
    override val resume: EResume,
    override val initialize: EInitialize,
    override val clear: EClear,

    open val run: ERunUntil,
    open val cycle: ECycle,
    open val iteration: EIteration,

    override val cost: ECost,
    override val costOfEdge: ECostOfEdge,
    override val costOfObjective: ECostOfObjective,

    open val initializePopulation: EInicializePopulation,
    open val orderByCost: EOrderPopulationByCost,
    open val boost: EBoost
): AAlgorithm4VRPSetup(pause, resume, initialize, clear, cost, costOfEdge, costOfObjective)
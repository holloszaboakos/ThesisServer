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

enum class EGeneticSetup(val code: String, val setup: GeneticAlgorithmSetup) {
    OPT2_HIBDRID(
        "statisticalRaceBased", GeneticAlgorithmSetup(
            EPause.DEFAULT,
            EResume.DEFAULT,
            EInitialize.EVOLUTIONARY,
            EClear.EVOLUTIONARY,
            ERunUntil.DEFAULT,
            ECycle.DEFAULT,
            EIteration.DEFAULT,
            ECost.NO_CAPACITY,
            ECostOfEdge.DEFAULT,
            ECostOfObjective.DEFAULT,
            EInicializePopulation.MODULO_STEPPER,
            EOrderPopulationByCost.RECALC_ALL,
            EBoost.OPT2_STEP,
            ESelectSurvivors.RANDOM,
            ECrossOvers.ORDERED,
            ECrossOverOperator.STATISTICAL_RACE,
            EMutateChildren.REVERSE,
        )
    )

}

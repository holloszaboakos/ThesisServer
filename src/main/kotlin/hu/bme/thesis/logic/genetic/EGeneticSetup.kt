package hu.bme.thesis.logic.genetic

import hu.bme.thesis.logic.genetic.control.ECycle
import hu.bme.thesis.logic.genetic.control.EIteration
import hu.bme.thesis.logic.genetic.control.ERunUntil
import hu.bme.thesis.logic.genetic.lifecycle.EPause
import hu.bme.thesis.logic.genetic.lifecycle.EResume
import hu.bme.thesis.logic.genetic.lifecycle.EInitialize
import hu.bme.thesis.logic.genetic.lifecycle.EClear
import hu.bme.thesis.logic.genetic.steps.*

enum class EGeneticSetup(val code: String, val setup: GeneticAlgorithmSetup) {
    OPT2_HIBDRID(
        "statisticalRaceBased", GeneticAlgorithmSetup(
            EPause.STANDARD,
            EResume.STANDARD,
            EInitialize.STANDARD,
            EClear.STANDARD,
            ERunUntil.STANDARD,
            ECycle.STANDARD,
            EIteration.STANDARD,
            EInicializePopulation.RANDOM,
            ECost.NO_CAPACITY,
            EOrderPopulationByCost.RECALC_ALL,
            EBoost.OPT2,
            ESelectSurvivers.RANDOM,
            ECrossOvers.ORDERED,
            ECrossOverOperator.STATISTICAL_PROBABILITY,
            EMutateChildren.REVERSE,
        )
    )

}
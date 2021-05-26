package thesis.logic.genetic

import thesis.logic.genetic.control.ECycle
import thesis.logic.genetic.control.EIteration
import thesis.logic.genetic.control.ERunUntil
import thesis.logic.genetic.lifecycle.EPause
import thesis.logic.genetic.lifecycle.EResume
import thesis.logic.genetic.lifecycle.EInitialize
import thesis.logic.genetic.lifecycle.EClear
import thesis.logic.genetic.steps.*

enum class EGeneticSetup(val code: String, val setup: GeneticAlgorithmSetup) {
    OPT2_HIBDRID(
        "geneticWithOpt2", GeneticAlgorithmSetup(
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
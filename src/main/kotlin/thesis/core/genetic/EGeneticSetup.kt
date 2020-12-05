package thesis.core.genetic

import thesis.core.genetic.control.ECycle
import thesis.core.genetic.control.EIteration
import thesis.core.genetic.control.ERunUntil
import thesis.core.genetic.lifecycle.EPause
import thesis.core.genetic.lifecycle.EResume
import thesis.core.genetic.lifecycle.EInitialize
import thesis.core.genetic.lifecycle.EClear
import thesis.core.genetic.steps.*

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
            ECrossOvers.HALF,
            EMutateChildren.REVERSE,
        )
    )

}
package hu.bme.thesis.model.inner.setup

import hu.bme.thesis.logic.common.lifecycle.EClear
import hu.bme.thesis.logic.common.lifecycle.EInitialize
import hu.bme.thesis.logic.common.lifecycle.EPause
import hu.bme.thesis.logic.common.lifecycle.EResume
import hu.bme.thesis.logic.common.steps.ECost
import hu.bme.thesis.logic.common.steps.ECostOfEdge
import hu.bme.thesis.logic.common.steps.ECostOfObjective

abstract class AAlgorithm4VRPSetup (
    open val pause: EPause,
    open val resume: EResume,
    open val initialize: EInitialize,
    open val clear: EClear,
    open val cost: ECost,
    open val costOfEdge: ECostOfEdge,
    open val costOfObjective: ECostOfObjective,
    )
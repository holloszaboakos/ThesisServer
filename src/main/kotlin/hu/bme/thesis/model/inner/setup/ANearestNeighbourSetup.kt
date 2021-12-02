package hu.bme.thesis.model.inner.setup

import hu.bme.thesis.logic.common.lifecycle.EClear
import hu.bme.thesis.logic.common.lifecycle.EInitialize
import hu.bme.thesis.logic.common.lifecycle.EPause
import hu.bme.thesis.logic.common.lifecycle.EResume
import hu.bme.thesis.logic.common.steps.ECost
import hu.bme.thesis.logic.common.steps.ECostOfEdge
import hu.bme.thesis.logic.common.steps.ECostOfObjective

data class DNearestNeighbourSetup(
    override val pause: EPause,
    override val resume: EResume,
    override val initialize: EInitialize,
    override val clear: EClear,
    override val cost: ECost,
    override val costOfEdge: ECostOfEdge,
    override val costOfObjective: ECostOfObjective,
    ):AAlgorithm4VRPSetup(pause, resume, initialize, clear, cost, costOfEdge, costOfObjective)
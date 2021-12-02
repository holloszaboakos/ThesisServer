package hu.bme.thesis.logic.common.steps

import hu.bme.thesis.model.mtsp.DObjective
import hu.bme.thesis.model.mtsp.DSalesman

enum class ECostOfObjective {
    DEFAULT{
        override fun invoke(objective: DObjective, salesman: DSalesman) =
            salesman.payment_EuroPerSecond * objective.time_Second
    };
    abstract operator fun invoke(objective: DObjective, salesman: DSalesman) : Double
}
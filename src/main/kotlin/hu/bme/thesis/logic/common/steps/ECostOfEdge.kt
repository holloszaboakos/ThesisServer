package hu.bme.thesis.logic.common.steps

import hu.bme.thesis.model.mtsp.DEdge
import hu.bme.thesis.model.mtsp.DSalesman

enum class ECostOfEdge {
    DEFAULT{
        override fun invoke(edge: DEdge, salesman: DSalesman) =
            salesman.fuelPrice_EuroPerLiter * salesman.fuelConsuption_LiterPerMeter * edge.length_Meter +
                    salesman.payment_EuroPerSecond * edge.length_Meter / salesman.vechicleSpeed_MeterPerSecond

    };
    abstract operator fun invoke(edge: DEdge, salesman: DSalesman) : Double
}
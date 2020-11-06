package data.otp

import java.util.*

class Plan(
    var date: Long? = null,
    var itineraries: List<Itinerary> = listOf(),
    var from: Place? = null,
    var to: Place? = null
)
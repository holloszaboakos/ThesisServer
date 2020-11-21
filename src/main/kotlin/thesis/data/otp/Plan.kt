package thesis.data.otp

class Plan(
    var date: Long? = null,
    var itineraries: List<Itinerary> = listOf(),
    var from: Place? = null,
    var to: Place? = null
)
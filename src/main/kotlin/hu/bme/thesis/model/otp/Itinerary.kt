package hu.bme.thesis.model.otp

data class Itinerary (
    var fare: Fare? = null,
    var walkDistance: Double? = null,
    var transitTime: Long? = null,
    var walkTime: Long? = null,
    var waitingTime: Long? = null,
    var walkLimitExceeded: Boolean? = null,
    var elevationGained: Double? = null,
    var tooSloped: Boolean? = null,
    var duration: Long? = null,
    var transfers: Int? = null,
    var legs: List<Leg> = listOf(),
    var elevationLost: Double? = null,
    var startTime: Long? = null,
    var endTime: Long? = null
    )
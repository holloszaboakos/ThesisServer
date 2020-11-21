package thesis.data.otp

data class Step(
    var area: Boolean? = null,
    var elevation: List<P2<Double>>? = null,
    var exit: String? = null,
    var streetName: String? = null,
    var distance: Double? = null,
    var bogusName: Boolean? = null,
    var stayOn: Boolean? = null,
    var lon: Double? = null,
    var absoluteDirection: AbsoluteDirection ? = null,
    var relativeDirection: RelativeDirection? = null,
    var lat: Double? = null,
    var alerts:List<LocalizedAlert> = listOf(),
    var angle:Double?=null
)
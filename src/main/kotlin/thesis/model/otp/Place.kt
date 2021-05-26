package thesis.model.otp

data class Place(
    var stopIndex: Int? = null,
    var stopCode: String? = null,
    var stopSequence: Int? = null,
    var arrival: Long? = null,
    var stopId: FeedScopedId? = null,
    var lon: Double? = null,
    var vertexType: VertexType? = null,
    var boardAlightType: BoardAlightType? = null,
    var orig: String? = null,
    var name: String? = null,
    var zoneId: String? = null,
    var departure: Long? = null,
    var flagStopArea: EncodedPolylineBean? = null,
    var platformCode: String? = null,
    var lat: Double? = null,
    var bikeShareId: String? = null
)
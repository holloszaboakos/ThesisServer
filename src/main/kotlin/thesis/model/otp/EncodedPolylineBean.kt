package thesis.model.otp


data class EncodedPolylineBean (
    /**
     * The encoded points of the polyline.
     */
    var points: String? = null,

    /**
     * Levels describes which points should be shown at various zoom levels. Presently, we show all
     * points at all zoom levels.
     */
    var levels: String? = null,

    /**
     * The number of points in the string
     */
    var length : Int = 0
)

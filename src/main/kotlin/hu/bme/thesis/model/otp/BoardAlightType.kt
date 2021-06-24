package hu.bme.thesis.model.otp

enum class BoardAlightType {
    /**
     * A regular boarding or alighting at a fixed-route transit stop.
     */
    DEFAULT,

    /**
     * A flag-stop boarding or alighting, e.g. flagging the bus down or a passenger asking the bus
     * driver for a drop-off between stops. This is specific to GTFS-Flex.
     */
    FLAG_STOP,

    /**
     * A boarding or alighting at which the vehicle deviates from its fixed route to drop off a
     * passenger. This is specific to GTFS-Flex.
     */
    DEVIATED
}

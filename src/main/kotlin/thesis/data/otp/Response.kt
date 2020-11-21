package thesis.data.otp

data class Response(
    var debugOutput: DebugOutput? = null,
    var requestParameters: RequestParameters? = null,
    var error: OtpError? = null,
    var plan: Plan? = null,
    var elevationMetadata: ElevationMetadata? = null
)
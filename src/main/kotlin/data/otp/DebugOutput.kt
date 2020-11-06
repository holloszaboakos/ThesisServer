package data.otp

data class DebugOutput (
    var pathCalculationTime: String? = null,
    var timedOut: String? = null,
    var precalculationTime: String? = null,
    var totalTime: String? = null,
    var renderingTime: String? = null,
    var pathTimes: List<Long> = listOf()
){

    override fun toString(): String {
        return "ClassPojo [pathCalculationTime = $pathCalculationTime, timedOut = $timedOut, precalculationTime = $precalculationTime, totalTime = $totalTime, renderingTime = $renderingTime, pathTimes = $pathTimes]"
    }
}
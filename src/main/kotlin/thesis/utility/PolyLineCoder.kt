package thesis.utility

import thesis.model.otp.EncodedPolylineBean
import thesis.model.mtsp.DGps
import org.locationtech.jts.geom.*
import java.math.BigDecimal
import java.util.*


object PolylineEncoder {
    fun createEncodings(lat: DoubleArray, lon: DoubleArray): EncodedPolylineBean {
        return createEncodings(PointAdapterList(lat, lon))
    }

    fun createEncodings(lat: DoubleArray, lon: DoubleArray, level: Int): EncodedPolylineBean {
        return createEncodings(PointAdapterList(lat, lon), level)
    }

    fun createEncodings(
        lat: DoubleArray, lon: DoubleArray, offset: Int,
        length: Int, level: Int
    ): EncodedPolylineBean {
        return createEncodings(PointAdapterList(lat, lon, offset, length), level)
    }

    fun createEncodings(points: Iterable<DGps?>): EncodedPolylineBean {
        return createEncodings(points, -1)
    }

    fun createEncodings(geometry: Geometry): EncodedPolylineBean {
        return if (geometry is LineString) {
            val string: LineString = geometry
            val coordinates: Array<DGps?> = string.getCoordinates().map { coordinate ->
                DGps(
                    lattitude = BigDecimal(coordinate.x),
                    longitude = BigDecimal(coordinate.y)
                )
            }.toTypedArray()
            createEncodings(CoordinateList(coordinates))
        } else if (geometry is MultiLineString) {
            val mls = geometry
            createEncodings(CoordinateList(mls.coordinates.map { coordinate ->
                DGps(
                    lattitude = BigDecimal(coordinate.x),
                    longitude = BigDecimal(coordinate.y)
                )
            }.toTypedArray()))
        } else {
            throw IllegalArgumentException(geometry.toString())
        }
    }

    /**
     * If level < 0, then [EncodedPolylineBean.getLevels] will be null.
     *
     * @param points
     * @param level
     * @return
     */
    fun createEncodings(points: Iterable<DGps?>, level: Int): EncodedPolylineBean {
        val encodedPoints = StringBuilder()
        val encodedLevels = StringBuilder()
        var plat = 0
        var plng = 0
        var count = 0
        points.filterNotNull().forEach {point ->
            val late5 = floor1e5(point.lattitude.toDouble())
            val lnge5 = floor1e5(point.longitude.toDouble())
            val dlat = late5 - plat
            val dlng = lnge5 - plng
            plat = late5
            plng = lnge5
            encodedPoints.append(encodeSignedNumber(dlat)).append(encodeSignedNumber(dlng))
            if (level >= 0) encodedLevels.append(encodeNumber(level))
            count++
        }
        val pointsString = encodedPoints.toString()
        val levelsString = if (level >= 0) encodedLevels.toString() else null
        return EncodedPolylineBean(pointsString, levelsString, count)
    }

    fun decode(pointString: String): List<DGps> {
        var lat = 0.0
        var lon = 0.0
        var strIndex = 0
        val points: MutableList<DGps> = ArrayList<DGps>()
        while (strIndex < pointString.length) {
            val rLat = decodeSignedNumberWithIndex(pointString, strIndex)
            lat += rLat[0] * 1e-5
            strIndex = rLat[1]
            val rLon = decodeSignedNumberWithIndex(pointString, strIndex)
            lon += rLon[0] * 1e-5
            strIndex = rLon[1]
            points.add(DGps(lattitude = BigDecimal(lat), longitude = BigDecimal(lon)))
        }
        return points
    }

    /*****************************************************************************
     * Private Methods
     */
    private fun floor1e5(coordinate: Double): Int {
        return Math.floor(coordinate * 1e5).toInt()
    }

    fun encodeSignedNumber(num: Int): String {
        var sgn_num = num shl 1
        if (num < 0) {
            sgn_num = sgn_num.inv()
        }
        return encodeNumber(sgn_num)
    }

    fun decodeSignedNumber(value: String): Int {
        val r = decodeSignedNumberWithIndex(value, 0)
        return r[0]
    }

    fun decodeSignedNumberWithIndex(value: String, index: Int): IntArray {
        val r = decodeNumberWithIndex(value, index)
        var sgn_num = r[0]
        if (sgn_num and 0x01 > 0) {
            sgn_num = sgn_num.inv()
        }
        r[0] = sgn_num shr 1
        return r
    }

    fun encodeNumber(num_: Int): String {
        var num = num_
        val encodeString = StringBuffer()
        while (num >= 0x20) {
            val nextValue = (0x20 or (num and 0x1f)) + 63
            encodeString.append(nextValue.toChar())
            num = num shr 5
        }
        num += 63
        encodeString.append(num.toChar())
        return encodeString.toString()
    }

    fun decodeNumber(value: String): Int {
        val r = decodeNumberWithIndex(value, 0)
        return r[0]
    }

    fun decodeNumberWithIndex(value: String, index: Int): IntArray {
        var indexCounter = index
        require(value.isNotEmpty()) { "string is empty" }
        var num = 0
        var v : Int
        var shift = 0
        do {
            v = value[indexCounter++].toInt() - 63
            num = num or (v and 0x1f shl shift)
            shift += 5
        } while (v >= 0x20)
        return intArrayOf(num, indexCounter)
    }

    class PointAdapterList @JvmOverloads constructor(
        private val lat: DoubleArray,
        private val lon: DoubleArray,
        private val offset: Int = 0,
        private val length: Int = lat.size
    ) : AbstractList<DGps?>() {

        override fun get(index: Int): DGps {
            return DGps(lattitude = BigDecimal(lat[offset + index]), longitude = BigDecimal(lon[offset + index]))
        }

        override val size: Int
            get() = length

    }

    private class CoordinateList(coordinates: Array<DGps?>) : AbstractList<DGps?>() {
        private var coordinates: Array<DGps?>
        override fun get(index: Int): DGps? {
            return coordinates[index]
        }

        override var size: Int
            get() = coordinates.size
            set(value) {
                coordinates = Array(value) { null }
            }


        init {
            this.coordinates = coordinates
        }
    }
}
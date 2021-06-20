package thesis

import com.google.gson.Gson
import thesis.model.mtsp.DGps
import thesis.model.mtsp.DSetup
import java.io.File
import java.math.BigDecimal

fun main() {
    val f = File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\wapromAddress.csv")
    var header: List<String>
    var indexOfLocationLatitude = 0
    var indexOfLocationLongitude = 0
    var firstLine = true
    val gps = mutableListOf<DGps>()
    f.forEachLine { line ->
        if (firstLine) {
            header = line.split(';')
            indexOfLocationLatitude = header.indexOf(header.find { it.compareTo("locationLatitude") == 0 })
            indexOfLocationLongitude = header.indexOf(header.find { it.compareTo("locationLongitude") == 0 })
            firstLine = !firstLine
        } else {
            val data = line.split(';')
            if (data[indexOfLocationLatitude].compareTo("NULL") != 0) {
                val latitude = data[indexOfLocationLatitude].toBigDecimal()
                val longitude = data[indexOfLocationLongitude].toBigDecimal()
                gps.add(DGps(lattitude = latitude,longitude = longitude))
            }
        }
    }
    gps.forEach {

    }

}
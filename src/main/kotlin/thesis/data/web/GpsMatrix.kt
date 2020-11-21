package thesis.data.web

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "gpsMatrix")
data class GpsMatrix(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    val gpsArrays: Array<GpsArray> = arrayOf()
) {
    init {
        gpsArrays.forEachIndexed { index, value ->
            value.owner = this
            value.orderInOwner = index
        }
    }
    operator fun get(index: Int) = gpsArrays[index]
    operator fun set(index: Int, value: GpsArray) {
        gpsArrays[index] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GpsMatrix

        if (id != other.id) return false
        if (!gpsArrays.contentEquals(other.gpsArrays)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + gpsArrays.contentHashCode()
        return result
    }
}
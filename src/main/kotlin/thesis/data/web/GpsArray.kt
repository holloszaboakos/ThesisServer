package thesis.data.web

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "gpsList")
data class GpsArray(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    var orderInOwner:Int = 0,
    @ManyToOne
    var owner:GpsMatrix?=null,
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    val gps: Array<Gps> = arrayOf()
) {
    init {
        gps.forEachIndexed { index, value ->
            value.owner = this
            value.orderInOwner = index
        }
    }
    operator fun get(index: Int) = gps[index]
    operator fun set(index: Int, value: Gps) {
        gps[index] = value
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GpsArray

        if (id != other.id) return false
        if (orderInOwner != other.orderInOwner) return false
        if (owner != other.owner) return false
        if (!gps.contentEquals(other.gps)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + orderInOwner
        result = 31 * result + owner.hashCode()
        result = 31 * result + gps.contentHashCode()
        return result
    }
}
/**
 * DRP
 * This is the hu.bme.thesis.utility.main server of QLM's DRP system
 *
 * OpenAPI spec version: 1.0.0
 * Contact: akos.hollo-szabo@qlndc.hu
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package hu.bme.thesis.model.mtsp

import java.util.*
import javax.persistence.*

/**
 *
 * @param id
 * @param name
 * @param length_Meter
 * @param root
 */
@Entity
@Table(name = "edge")
@NamedQueries(
    NamedQuery(
        name = "listEdge",
        query = "FROM Edge"
    ),
    NamedQuery(
        name = "findByNameEdge",
        query = "FROM Edge where name = :name"
    )
)
data class DEdge(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    val name: String = "",

    var orderInOwner: Int = 0,

    val length_Meter: Long = 0L,
    @OneToMany(cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    val route: Array<DGps> = arrayOf()
) {
    init {
        route.forEachIndexed { index, gps -> gps.orderInOwner = index }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DEdge

        if (id != other.id) return false
        if (name != other.name) return false
        if (orderInOwner != other.orderInOwner) return false
        if (length_Meter != other.length_Meter) return false
        if (!route.contentEquals(other.route)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + orderInOwner
        result = 31 * result + length_Meter.hashCode()
        result = 31 * result + route.contentHashCode()
        return result
    }

}



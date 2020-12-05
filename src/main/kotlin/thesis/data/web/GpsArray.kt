package thesis.data.web

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "gps_array")
@NamedQueries(
    NamedQuery(
        name = "listGpsArray",
        query = "FROM GpsArray"
    ),
    NamedQuery(
        name = "findByNameGpsArray",
        query = "FROM GpsArray where name = :name"
    )
)
data class GpsArray(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    var orderInOwner : Int = 0,
    @OneToMany(cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    var values: Array<Gps> = arrayOf()
){
    init {
        values.forEachIndexed { index, gps -> gps.orderInOwner = index }
    }
}

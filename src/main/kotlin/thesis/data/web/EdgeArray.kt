package thesis.data.web

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "edge_array")
@NamedQueries(
    NamedQuery(
        name = "listEdgeArray",
        query = "FROM EdgeArray"
    ),
    NamedQuery(
        name = "findByNameEdgeArray",
        query = "FROM EdgeArray where name = :name"
    )
)
data class EdgeArray(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    var orderInOwner : Int = 0,
    @OneToMany(cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    var values: Array<Edge> = arrayOf()
){
    init {
        values.forEachIndexed { index, gps -> gps.orderInOwner = index }
    }
}

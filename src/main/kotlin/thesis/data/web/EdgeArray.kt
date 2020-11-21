package thesis.data.web

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "edgeList")
data class EdgeArray(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    @ManyToOne
    var owner: EdgeMatrix?=null,
    var orderInOwner:Int = 0,
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    val edges: Array<Edge> = arrayOf()
) {
    init {
        edges.forEachIndexed { index, value ->
            value.owner = this
            value.orderInOwner = index
        }
    }
    operator fun get(index: Int) = edges[index]
    operator fun set(index: Int, value: Edge) {
        edges[index] = value
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EdgeArray

        if (id != other.id) return false
        if (owner != other.owner) return false
        if (orderInOwner != other.orderInOwner) return false
        if (!edges.contentEquals(other.edges)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + owner.hashCode()
        result = 31 * result + orderInOwner
        result = 31 * result + edges.contentHashCode()
        return result
    }
}
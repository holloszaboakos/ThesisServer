package thesis.data.web

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "edgeMatrix")
data class EdgeMatrix(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    val edgesArrays: Array<EdgeArray> = arrayOf()
) {
    init {
        edgesArrays.forEachIndexed { index, value ->
            value.owner = this
            value.orderInOwner = index
        }
    }

    operator fun get(index: Int) = edgesArrays[index]
    operator fun set(index: Int, value: EdgeArray) {
        edgesArrays[index] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EdgeMatrix

        if (id != other.id) return false
        if (!edgesArrays.contentEquals(other.edgesArrays)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + edgesArrays.contentHashCode()
        return result
    }
}
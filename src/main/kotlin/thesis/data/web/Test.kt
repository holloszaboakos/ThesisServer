package thesis.data.web

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "test")
@NamedQueries(
    NamedQuery(
        name = "listTest",
        query = "FROM Test"
    ),
    NamedQuery(
        name = "findByNameTest",
        query = "FROM Test where name = :name"
    )
)
data class Test(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    @OneToMany(cascade = [CascadeType.ALL])
    @OrderColumn(name = "orderInOwner")
    val items: Array<TestItem> = arrayOf(),
) {
    init {
        items.forEachIndexed { index, testItem -> testItem.orderInOwner = index }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Test

        if (id != other.id) return false
        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + items.contentHashCode()
        return result
    }
}

package thesis.data.web

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "test_item")
@NamedQueries(
    NamedQuery(
        name = "listTestItem",
        query = "FROM TestItem"
    ),
    NamedQuery(
        name = "findByNameTestItem",
        query = "FROM TestItem where name = :name"
    )
)
data class TestItem(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    var value : Int = 0,
    var orderInOwner : Int = 0
)

package thesis

import org.hibernate.Hibernate
import thesis.data.HibernateManager
import thesis.data.web.Edge
import thesis.data.web.GpsArray
import java.math.BigDecimal

fun main() {
    HibernateManager.save(
        Edge(
            id = "edge",
            name = "edge",
            length_Meter = BigDecimal(0),
            rout = GpsArray()
        )
    )
    HibernateManager.deleteById<Edge>("edge")
}
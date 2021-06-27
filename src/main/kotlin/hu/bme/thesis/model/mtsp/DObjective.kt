/**
* DRP
* This is the hu.bme.thesis.main server of QLM's DRP system
*
* OpenAPI spec version: 1.0.0
* Contact: akos.hollo-szabo@qlndc.hu
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package hu.bme.thesis.model.mtsp

import java.math.BigDecimal
import java.util.*
import javax.persistence.*

/**
 * 
 * @param id 
 * @param name 
 * @param location 
 * @param time_Second 
 * @param volume_Stere 
 * @param weight_Gramm
 */
@Entity
@Table(name = "objective")
@NamedQueries(
    NamedQuery(
        name = "listObjective",
        query = "FROM Objective"
    ),
    NamedQuery(
        name = "findByNameObjective",
        query = "FROM Objective where name = :name"
    )
)
data class DObjective (
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    val name: String = "",
    var orderInOwner: Int =0,
    @OneToOne(cascade = [CascadeType.ALL])
    val location: DGps = DGps(),
    val time_Second: Long = 0L,
    val volume_Stere: Long = 0L,
    val weight_Gramm: Long = 0L
)

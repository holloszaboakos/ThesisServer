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

import java.math.BigDecimal
import java.util.*
import javax.persistence.*


/**
 * 
 * @param id 
 * @param name 
 * @param iterLimit 
 * @param timeLimit_Second 
 * @param algorithm 
 */
@Entity
@Table(name = "setting")
@NamedQueries(
    NamedQuery(
        name = "listSetting",
        query = "FROM Setting"
    ),
    NamedQuery(
        name = "findByNameSetting",
        query = "FROM Setting where name = :name"
    )
)
data class DSetting (
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    val name: String="",
    val iterLimit: BigDecimal= BigDecimal(0),
    val timeLimit_Second: BigDecimal=BigDecimal(0),
    val algorithm: String=""
)


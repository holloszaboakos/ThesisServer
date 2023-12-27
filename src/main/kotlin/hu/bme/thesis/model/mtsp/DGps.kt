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
 * @param latitude
 * @param longitude 
 */
@Entity
@Table(name = "gps")
@NamedQueries(
    NamedQuery(
        name = "listGps",
        query = "FROM Gps"
    ),
    NamedQuery(
        name = "findByNameGps",
        query = "FROM Gps where name = :name"
    )
)
data class DGps(
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    var orderInOwner: Int = 0,
    val latitude: Float = 0.0f,
    val longitude: Float = 0.0f
) {

}


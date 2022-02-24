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
 * @param workTime_SecondPerDay 
 * @param volumeCapacity_Stere 
 * @param weightCapacity_Gramm 
 * @param vechicleSpeed_MeterPerSecond 
 * @param payment_EuroPerSecond 
 * @param fuelConsuption_LiterPerMeter 
 * @param fuelPrice_EuroPerLiter 
 * @param basePrice_Euro 
 */
@Entity
@Table(name = "salesman")
@NamedQueries(
    NamedQuery(
        name = "listSalesman",
        query = "FROM Salesman"
    ),
    NamedQuery(
        name = "findByNameSalesman",
        query = "FROM Salesman where name = :name"
    )
)
data class DSalesman (
    @Id
    @Column(name = "id", length = 255)
    var id: String = UUID.randomUUID().toString(),
    val name: String="",
    var orderInOwner: Int=0,
    val workTime_SecondPerDay: Long= 0L,

    val volumeCapacity_Stere: Long= 0L,
    val weightCapacity_Gramm: Long= 0L,

    val vechicleSpeed_MeterPerSecond: Long= 0L,

    val payment_EuroPerSecond: Double= 0.0,

    val fuelConsuption_LiterPerMeter: Double= 0.0,
    val fuelPrice_EuroPerLiter: Double= 0.0,

    val basePrice_Euro: Double=0.0
)


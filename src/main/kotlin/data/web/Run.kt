/**
* DRP
* This is the main server of QLM's DRP system
*
* OpenAPI spec version: 1.0.0
* Contact: akos.hollo-szabo@qlndc.hu
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/
package data.web


/**
 * 
 * @param id 
 * @param name 
 * @param iteration 
 * @param runtime_Second 
 */
data class Run (
    val id: kotlin.String,
    val name: kotlin.String,
    val iteration: java.math.BigDecimal,
    val runtime_Second: java.math.BigDecimal
) {

}

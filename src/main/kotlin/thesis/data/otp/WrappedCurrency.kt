package thesis.data.otp

import java.util.*


data class WrappedCurrency(
    var currency: Currency?
){
    constructor(name: String?) : this(Currency.getInstance(name))

    val defaultFractionDigits: Int
        get() = currency!!.defaultFractionDigits
    val currencyCode: String
        get() = currency!!.currencyCode
    val symbol: String
        get() = currency!!.symbol

    fun getSymbol(l: Locale?): String {
        return currency!!.getSymbol(l)
    }
}

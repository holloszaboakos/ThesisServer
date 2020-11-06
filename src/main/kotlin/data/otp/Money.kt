package data.otp

import java.text.NumberFormat
import java.util.*


data class Money (
    /**
     * The currency of the money.
     */
    var currency: WrappedCurrency? = null,

    /**
     * The actual currency value in decimal fixed-point, with the default number of fraction digits
     * from currency after the decimal point.
     */
    var cents : Int = 0
)

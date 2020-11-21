package thesis.data.otp

import java.util.*


data class Fare(

    /**
     * A mapping from [FareType] to [Money].
     */
    var fare: HashMap<FareType, Money> = HashMap(),

    /**
     * A mapping from [FareType] to a list of [FareComponent].
     * The FareComponents are stored in an array instead of a list because JAXB doesn't know how to deal with
     * interfaces when serializing a trip planning response, and List is an interface.
     * See https://stackoverflow.com/a/1119241/778449
     */
    var details: HashMap<FareType, Array<FareComponent>> = HashMap()
)

package hu.bme.thesis.model.otp

import java.util.*


data class LocalizedAlert (
    
    val alertHeaderText: String? = null,
    
    val alertDescriptionText: String? = null,
    
    val alertUrl: String? = null,
    
    val effectiveStartDate: Date
)

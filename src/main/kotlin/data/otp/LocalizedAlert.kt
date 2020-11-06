package data.otp

import javafx.scene.control.Alert
import java.util.*


data class LocalizedAlert (
    
    val alertHeaderText: String? = null,
    
    val alertDescriptionText: String? = null,
    
    val alertUrl: String? = null,
    
    val effectiveStartDate: Date
)

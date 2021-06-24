package hu.bme.thesis.model.otp

class OtpError {
    var msg: String? = null
    var noPath: String? = null
    var id: String? = null
    var message: String? = null

    override fun toString(): String {
        return "ClassPojo [msg = $msg, noPath = $noPath, id = $id, message = $message]"
    }
}
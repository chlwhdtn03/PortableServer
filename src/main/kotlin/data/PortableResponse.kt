package data

@kotlinx.serialization.Serializable
data class PortableResponse(
    var code:Int,
    var success: Boolean,
    var message:String
)

package data

@kotlinx.serialization.Serializable
data class RouterListener(
    var name: String, // 별명
    var address: String, // 하위 주소
    var type: RouterType // GET, POST, GET&POST
)

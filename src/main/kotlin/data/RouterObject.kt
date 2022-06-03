package data

@kotlinx.serialization.Serializable
data class RouterObject(
    var name: String, // 별명
    val address: String, // 하위 주소
    val type: RouterType, // GET, POST, GET&POST
    val target_object: PortableObject? // 이거 Null이면 message에 꼭 값 필요함
) {
    var message:String = ""

    constructor(name: String, address: String, type: RouterType, target_object: PortableObject?, message:String) : this(name, address, type, target_object) {
        this.message = message
    }
}

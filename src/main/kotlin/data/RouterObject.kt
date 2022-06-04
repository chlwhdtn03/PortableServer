package data

@kotlinx.serialization.Serializable
data class RouterObject(
    var name: String, // 별명
    val address: String, // 하위 주소
    val type: RouterMethod, // GET, POST, GET&POST
    val target_object: PortableObject?, // 이거 Null이면 message에 꼭 값 필요함
    val target_trigger: TriggerType?,
    val target_data: String? // 데이터 검증의 경우, 어떤 데이터를 확인할 것인지. 그 데이터의 변수명
) {
    var message:String = ""

    constructor(name: String, address: String, type: RouterMethod, target_object: PortableObject?, target_trigger: TriggerType?, target_data: String?, message:String)
            : this(name, address, type, target_object, target_trigger, target_data) {
        this.message = message
    }
}

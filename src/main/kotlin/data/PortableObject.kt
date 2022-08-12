package data

import kotlinx.serialization.Serializable
import javax.swing.JList
import javax.swing.JTextField

@Serializable
data class PortableObject(
    var name: String, // 오브젝트 명
    val varNames: Array<String>,
    val varTypes: Array<String>,
    val varProvide: Array<Boolean> // TriggerType에서 GET_DATA의 경우, 제공 안할 값은 False로 설정됨
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PortableObject

        if (name != other.name) return false
        if (!varNames.contentEquals(other.varNames)) return false
        if (!varTypes.contentEquals(other.varTypes)) return false
        if (!varProvide.contentEquals(other.varProvide)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + varNames.contentHashCode()
        result = 31 * result + varTypes.contentHashCode()
        result = 31 * result + varProvide.contentHashCode()
        return result
    }
}
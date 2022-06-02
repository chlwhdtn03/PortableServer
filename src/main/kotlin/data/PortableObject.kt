package data

import kotlinx.serialization.Serializable
import javax.swing.JList
import javax.swing.JTextField

@Serializable
data class PortableObject(
    var name: String, // 오브젝트 명
    val varNames: Array<String>,
    val varTypes: Array<String>
)
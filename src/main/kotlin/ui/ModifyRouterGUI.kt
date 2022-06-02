package ui

import data.PortableObject
import data.RouterObject
import file.FileManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.SystemColor
import javax.swing.*

class ModifyRouterGUI(parent: Bootstrap, title: String) : JFrame() {


    val titlePanel = JPanel();
    val field_name = JTextField(20)
    val tv_name = JLabel("라우터 별명")
    val tv_path = JLabel("라우터 주소")
    val field_path = JTextField(20)
    val tv_target = JLabel("타겟 오브젝트")
    val select_target = JComboBox<String>(FileManager.loadObjects().plus("타겟 없음"))

    var saveBtn: JButton = JButton("저장")

    constructor(parent: Bootstrap, obj: RouterObject) : this(parent, obj.name) {
        field_name.text = obj.name
        field_name.isEditable = false
    }

    init {
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(500, 500)
        setLocationRelativeTo(null)
        isResizable = false

        layout = FlowLayout()

        titlePanel.add(tv_name)
        titlePanel.add(field_name)

        setTitle(title)

        add(titlePanel)

        /** 변수 부분 **/



        saveBtn.addActionListener {


        }

        add(saveBtn)

        isVisible = true

    }



}
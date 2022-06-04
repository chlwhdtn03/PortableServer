package ui

import data.PortableObject
import file.FileManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.SystemColor
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JTextField

class ModifyObjectGUI(parent: Bootstrap, title: String) : JFrame() {


    val titlePanel = JPanel();
    val field_name = JTextField(20)
    val support_data_type: Array<String> = arrayOf("string", "int") // 추가 예정
    val tv_name = JLabel()


    var vaild_count = 0 // 저장될 변수 개수

    var varNames: Array<JTextField> =
        arrayOf(JTextField(20), JTextField(20), JTextField(20), JTextField(20), JTextField(20))
    var varTypes: Array<JList<String>> =
        arrayOf(JList(support_data_type), JList(support_data_type), JList(support_data_type), JList(support_data_type), JList(support_data_type))
    var varProvides: Array<JCheckBox> =
        arrayOf(JCheckBox().apply { isEnabled=false }, JCheckBox().apply { text = "보안 사용" }, JCheckBox().apply { text = "보안 사용" }, JCheckBox().apply { text = "보안 사용" }, JCheckBox().apply { text = "보안 사용" })

    var varNamesStr:Array<String> = arrayOf("","","","","")
    var varTypesStr:Array<String> = arrayOf("int","int","int","int","int")
    var varProvidesStr:Array<Boolean> = arrayOf(true,true,true,true,true)

    var saveBtn: JButton = JButton("save")
    var hintlabel: JLabel = JLabel("변수명을 빈칸으로 남겨두면 자동으로 제외됩니다.")

    private var editmode: Boolean = false

    constructor(parent: Bootstrap, obj: PortableObject) : this(parent, obj.name) {
        editmode = true
        field_name.text = obj.name
        field_name.isEditable = false
        for(i in 0..4) {
            varNames[i].text = obj.varNames[i]
            varTypes[i].setSelectedValue(obj.varTypes[i], false)
            varProvides[i].isSelected = !obj.varProvide[i]
        }
    }

    init {
        editmode = false
        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(500, 500)
        setLocationRelativeTo(null)
        isResizable = false

        layout = FlowLayout()

        tv_name.text = "객체 이름"
        titlePanel.add(tv_name)
        titlePanel.add(field_name)

        setTitle(title)

        add(titlePanel)

        /** 변수 부분 **/

        for(i in 0..4) {
            val panel = JPanel()
            val label = JLabel(if(i == 0) "(일반키)변수명 : " else "변수명")
            val label2 = JLabel("변수 타입 : ")

            add(panel.apply {
                add(label)

                add(varNames[i])
                add(label2)
                add(varTypes[i])
                add(varProvides[i])
                varTypes[i].selectedIndex = 0
            })
        }



        saveBtn.addActionListener {

            if(field_name.text.isEmpty()) {
                hintlabel.text = "객체 이름을 입력하세요"
                field_name.background = SystemColor.yellow
                return@addActionListener;
            }
            if(!editmode && parent.objects.any { it.name == field_name.text }) {

                hintlabel.text = "이미 존재하는 객체 이름"
                field_name.background = SystemColor.yellow
                return@addActionListener
            }

            if(!"^[^0-9][\\w]+\$".toRegex().matches(field_name.text)) {
                hintlabel.text = "올바른 객체 이름이 아닙니다."
                field_name.background = SystemColor.yellow
                return@addActionListener
            }

            vaild_count = 0
            for(i in 0..4) {
                if(varNames[i].text.isEmpty())
                    continue
                if(!"^[^0-9][\\w]+\$".toRegex().matches(varNames[i].text)) {
                    varNames[i].background = SystemColor.yellow
                    continue
                }
                varNamesStr[i] = varNames[i].text.trim()
                varTypesStr[i] = varTypes[i].selectedValue
                varProvidesStr[i] = !varProvides[i].isSelected

                vaild_count++
                // TODO: 중복되는 변수명에 대한 사용자에게 알림 필요
            }

            if(vaild_count > 0) {
                var data = PortableObject(field_name.text.trim(), varNamesStr, varTypesStr, varProvidesStr);
                println(data)
                val str = Json.encodeToString(data)
                FileManager.saveObject(field_name.text.trim(), str)
                parent.object_list.setListData(parent.loadPortableObjectList())
                dispose()
            } else {
                hintlabel.text = "최소 1개의 멤버가 필요합니다."
            }

        }


        add(hintlabel)
        add(saveBtn)

        isVisible = true

    }



}
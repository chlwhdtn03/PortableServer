package ui

import data.PortableObject
import data.RouterObject
import data.RouterMethod
import data.TriggerType
import util.FileManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.awt.Dimension
import java.awt.SystemColor
import javax.swing.*


class ModifyRouterGUI(parent: Bootstrap, title: String) : JFrame() {


    val titlePanel = JPanel();
    val field_name = JTextField(20)
    val tv_name = JLabel("라우터 별명")
    val tv_path = JLabel("라우터 주소")
    val tv_type = JLabel("라우터 타입")
    var checkbox_type: Array<JCheckBox> = emptyArray()
    val buttonGroup = ButtonGroup()
    val field_path = JTextField(20)
    val tv_target = JLabel("타겟 오브젝트")

    val message_tv = JLabel("타겟 오브젝트가 지정되지 않았습니다. response 메세지를 직접 정해주세요")
    val message_area = JTextArea(10,30).apply {
        lineWrap = true
    }
    val message_scroll = JScrollPane(message_area).apply {
        verticalScrollBar.setUI(PortableScrollbarUI())
        autoscrolls = true
    }

    val target_panel = JPanel()
    val target_trigger_panel = JPanel()
    val target_data_panel = JPanel()

    private var target_list: Array<String>
    private var select_target: JComboBox<String>
    private var select_trigger_data: JComboBox<String> = JComboBox<String>()
    private var select_trigger_type : JComboBox<String>

    var saveBtn: JButton = JButton("저장")
    var originRouter: RouterObject? = null
    constructor(parent: Bootstrap, obj: RouterObject) : this(parent, "${obj.name} (/${obj.address})") {
        originRouter = obj
        field_name.text = obj.name
        field_name.isEditable = false
        field_path.text = obj.address
        field_path.isEditable = false
        checkbox_type.forEach { checkbox ->
            checkbox.isEnabled = false
            if(obj.type.name == checkbox.text)
                checkbox.isSelected = true
        }
        field_path.isEditable = false


        if(obj.target_object == null) {
            message_area.text = obj.message

            target_trigger_panel.removeAll()
            target_data_panel.removeAll()
            target_panel.removeAll()
            target_panel.add(message_tv)
            target_panel.add(message_scroll)
        }

    }

    init {
        target_list = parent.loadPortableObjectList().plus("타겟 없음")
        select_target = JComboBox<String>(target_list)
        select_trigger_type = JComboBox<String>(TriggerType.values().map { triggerType -> triggerType.name }.toTypedArray())

        defaultCloseOperation = DISPOSE_ON_CLOSE
        size = Dimension(500, 400)
        setLocationRelativeTo(null)
        setTitle(title)

        layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)

        titlePanel.add(tv_name)
        titlePanel.add(field_name)
        add(titlePanel)

        var panel = JPanel()
        panel.add(tv_path)
        panel.add(field_path)
        add(panel)

        panel = JPanel()
        panel.add(tv_type)
        for(i in RouterMethod.values().indices) {
            checkbox_type = checkbox_type.plus(JCheckBox(RouterMethod.values()[i].name))
            buttonGroup.add(checkbox_type[i])
            panel.add(checkbox_type[i])
        }
        add(panel)


        target_panel.add(tv_target)
        target_panel.add(select_target)
        add(target_panel)

        target_trigger_panel.add(select_trigger_type)
        add(target_trigger_panel)

        target_data_panel.add(select_trigger_data)
        add(target_data_panel)

        /** 변수 부분 **/

        val temp = getSelectedPortableObject(select_target)
        if(temp != null)
            for( i in temp.varNames)
                if(i.isNotEmpty())
                    select_trigger_data.addItem(i)
        select_target.addItemListener {
            select_trigger_data.removeAllItems()
            val temp = getSelectedPortableObject(select_target)
            if(temp != null)
                for( i in temp.varNames)
                    if(i.isNotEmpty())
                        select_trigger_data.addItem(i)
        }


        saveBtn.addActionListener {
            if(field_name.text.isEmpty()) {
//                hintlabel.text = "객체 이름을 입력하세요"
                field_name.background = SystemColor.yellow
                return@addActionListener;
            }

            if(!"^[^0-9][\\w]+\$".toRegex().matches(field_path.text)) {
//                hintlabel.text = "올바른 객체 이름이 아닙니다."
                field_path.background = SystemColor.yellow
                return@addActionListener
            }

            if(getSelectedButtonText(buttonGroup) == "NULL") {
                buttonGroup.elements.asIterator().forEach {
                    it.background = SystemColor.yellow
                }
                return@addActionListener
            }

            originRouter?.let {
                it.message = message_area.text
                FileManager.saveRouter(it.name, Json.encodeToString(it))
                parent.loadRouterList()
                dispose()
                return@addActionListener;
            }

            val router = RouterObject(
                field_name.text,
                field_path.text,
                RouterMethod.valueOf(getSelectedButtonText(buttonGroup)),
                getSelectedPortableObject(select_target),
                select_trigger_type.selectedItem?.toString()?.let { it1 -> TriggerType.valueOf(it1) },
                select_trigger_data.selectedItem?.toString())

            FileManager.saveRouter(router.name, Json.encodeToString(router))
            parent.loadRouterList()
            dispose()

        }

        add(saveBtn)

        isVisible = true

    }

    private fun getSelectedPortableObject(selectTarget: JComboBox<String>): PortableObject? {

        return if(selectTarget.selectedIndex == selectTarget.itemCount-1) {
            null;
        } else {
            FileManager.loadObject(target_list[selectTarget.selectedIndex])
        }


    }

    private fun getSelectedButtonText(buttonGroup: ButtonGroup): String {
        val buttons = buttonGroup.elements
        while (buttons.hasMoreElements()) {
            val button = buttons.nextElement()
            if (button.isSelected) {
                return button.text
            }
        }
        return "NULL"
    }



}
package ui.setting

import data.Option
import javax.swing.JScrollPane
import javax.swing.JTextField

class PortPanel : OptionPanel() {

    init {

        val inputPort = JTextField(10)

        add(JScrollPane(getSettingPanel(
            Option("Port (HTTP:80, HTTPS:443)", inputPort)
        )))

    }
}
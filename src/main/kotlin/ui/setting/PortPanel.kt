package ui.setting

import data.Option
import java.text.NumberFormat
import javax.swing.JFormattedTextField
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.text.NumberFormatter


class PortPanel : OptionPanel() {

    init {
        val inputPort = JTextField(10)

        add(JScrollPane(getSettingPanel(
            Option("Port (HTTP:80, HTTPS:443)", inputPort)
        )))


    }
}
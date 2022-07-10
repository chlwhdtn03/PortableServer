package ui.setting

import data.Option
import java.text.NumberFormat
import javax.swing.JFormattedTextField
import javax.swing.JScrollPane
import javax.swing.text.NumberFormatter


class PortPanel : OptionPanel() {

    init {
        val format = NumberFormat.getIntegerInstance()
        format.isGroupingUsed = false
        val numberFormatter = NumberFormatter(format)
        numberFormatter.valueClass = Long::class.java
        numberFormatter.allowsInvalid = false //t

        val inputPort = JFormattedTextField(format)

        add(JScrollPane(getSettingPanel(
            Option("Port (HTTP:80, HTTPS:443)", inputPort)
        )))


    }
}
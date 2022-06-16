package ui.setting

import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.JToggleButton

class PortPanel : OptionPanel() {

    init {

        val inputPort = JTextField(10)

        add(JScrollPane(getSettingPanel(
            Option("Port (HTTP:80, HTTPS:443)", inputPort)
        )))

    }
}
package ui.setting

import data.Option
import data.PointerBoolean
import ui.PortableToggle
import java.awt.Color
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.JToggleButton

class SSLPanel : OptionPanel() {
    private val isSSL: PointerBoolean = PointerBoolean(false)
    init {

        val toggleSSL = PortableToggle(isSSL)

        val findKeystore = JButton("찾기")
        val findTruststore = JButton("찾기")

        add(JScrollPane(getSettingPanel(
            Option("SSL 사용", toggleSSL),
            Option("KeyStore 위치", findKeystore),
            Option("TrustStore 위치", findTruststore)
        )))

    }


}
package ui.setting

import data.Option
import javax.swing.JButton
import javax.swing.JScrollPane
import javax.swing.JToggleButton

class SSLPanel : OptionPanel() {
    init {

        val toggleSSL = JToggleButton("SSL 사용(HTTPS 사용)")

        val findKeystore = JButton("찾기")
        val findTruststore = JButton("찾기")

        add(JScrollPane(getSettingPanel(
            Option("SSL 사용", toggleSSL),
            Option("KeyStore 위치", findKeystore),
            Option("TrustStore 위치", findTruststore)
        )))

    }


}
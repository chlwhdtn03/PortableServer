package ui.setting

import org.pushingpixels.radiance.theming.api.ComponentState
import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices.DecorationAreaType
import org.pushingpixels.radiance.theming.ktx.getCurrentSkin
import ui.PortableScrollbarUI
import ui.VerticalStackLayout
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.FlowLayout
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JToggleButton
import javax.swing.ListSelectionModel
import javax.swing.border.Border

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
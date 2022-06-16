package ui

import org.pushingpixels.radiance.theming.api.ComponentState
import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices.DecorationAreaType
import ui.setting.SSLPanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JList
import javax.swing.ListSelectionModel

class SettingGUI : JFrame("Setting") {
    init {
        val DESTINATIONS = DecorationAreaType("Visor Destinations")
        size = Dimension(500,300)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        setLocationRelativeTo(null)

        layout = BorderLayout()

//        RadianceThemingCortex.WindowScope.extendContentIntoTitlePane(this, RadianceThemingSlices.HorizontalGravity.LEADING, RadianceThemingSlices.VerticalGravity.CENTERED)
        RadianceThemingCortex.WindowScope.setPreferredTitlePaneHeight(this, 40);

        val destinationList = JList<String>(
            arrayOf("SSL 인증서", "프로그램 테마", "포트", "고급 설정")
        )
        destinationList.cellRenderer = DestinationRenderer()

        destinationList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        destinationList.selectedIndex = 0

//        ComponentOrParentChainScope.setDecorationType(destinationList, DESTINATIONS)
        destinationList.background = RadianceThemingCortex.GlobalScope.getCurrentSkin().getColorScheme(
            DESTINATIONS, RadianceThemingSlices.ColorSchemeAssociationKind.FILL, ComponentState.ENABLED).darkColor

        add(destinationList, BorderLayout.LINE_START)
        add(SSLPanel(), BorderLayout.CENTER)

        isVisible = true
    }
}
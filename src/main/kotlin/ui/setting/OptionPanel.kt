package ui.setting

import data.Option
import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import ui.VerticalStackLayout
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JPanel

open class OptionPanel : JPanel() {

    init {
        val currentSkin = RadianceThemingCortex.GlobalScope.getCurrentSkin()
//        background = currentSkin.getColorScheme(
//            DecorationAreaType.NONE,
//            RadianceThemingSlices.ColorSchemeAssociationKind.FILL,
//            ComponentState.ENABLED
//        ).foregroundColor

        layout = BorderLayout()
        alignmentX = LEFT_ALIGNMENT
    }

    protected fun getSettingPanel(vararg options: Option) : JPanel {
        val result = JPanel()
        result.layout = VerticalStackLayout()
        alignmentX = LEFT_ALIGNMENT
        for(option in options) {
            result.add(JPanel().apply {
                add(JLabel(option.optionName))
                add(option.optionComponent)
            })
        }
        return result
    }

}
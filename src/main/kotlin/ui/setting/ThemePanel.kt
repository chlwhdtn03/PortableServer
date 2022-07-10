package ui.setting

import data.Option
import data.PointerBoolean
import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import ui.PortableToggle
import ui.SkinComboSelector
import util.OptionManager
import util.OptionObject
import javax.swing.JScrollPane

class ThemePanel : OptionPanel() {

    init {

        add(JScrollPane(
            getSettingPanel(
                Option("테마", SkinComboSelector())
            )
        ))

    }

}
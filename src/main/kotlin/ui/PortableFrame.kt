package ui

import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import org.pushingpixels.radiance.theming.api.skin.MistSilverSkin
import org.pushingpixels.radiance.theming.api.skin.SkinInfo
import util.OptionManager
import javax.swing.JFrame
import javax.swing.UIManager


open class PortableFrame : JFrame() {
    init {
        RadianceThemingCortex.GlobalScope.setSkin(OptionManager.THEME_SKIN)
    }
}
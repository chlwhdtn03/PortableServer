package ui

import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import org.pushingpixels.radiance.theming.api.renderer.RadianceDefaultComboBoxRenderer
import org.pushingpixels.radiance.theming.api.skin.SkinInfo
import util.OptionManager
import util.OptionObject
import java.awt.Component
import javax.swing.JComboBox
import javax.swing.JList
import javax.swing.SwingUtilities

class SkinComboSelector : JComboBox<Any?>(ArrayList(RadianceThemingCortex.GlobalScope.getAllSkins().values).toTypedArray()) {
    init {
        // populate the combobox
        // set the current skin as the selected item
        val currentSkin = RadianceThemingCortex.GlobalScope.getCurrentSkin()

        for (skinInfo in RadianceThemingCortex.GlobalScope.getAllSkins().values) {
            if (skinInfo.displayName.compareTo(currentSkin.displayName) == 0) {
                this.selectedItem = skinInfo
                break
            }
        }

        // set custom renderer to show the skin display name
        setRenderer(object : RadianceDefaultComboBoxRenderer(this) {
            override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any, index: Int, isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                return super.getListCellRendererComponent(
                    list,
                    (value as SkinInfo).displayName, index, isSelected,
                    cellHasFocus
                )
            }
        })

        // add an action listener to change skin based on user selection
        addActionListener {
            SwingUtilities.invokeLater {
                RadianceThemingCortex.GlobalScope
                    .setSkin(
                        (this@SkinComboSelector.selectedItem as SkinInfo)
                            .className
                    )
                OptionObject.THEME_SKIN = (this@SkinComboSelector.selectedItem as SkinInfo).className
                OptionManager.saveOptions()
            }
        }
    }
}
package ui.component

import org.pushingpixels.radiance.theming.api.renderer.RadiancePanelListCellRenderer
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JList

class DestinationRenderer : RadiancePanelListCellRenderer<String>() {
    private var titleLabel: JLabel = JLabel()

    init {
        registerThemeAwareLabelsWithText(titleLabel)
        layout = BorderLayout()
        add(titleLabel, BorderLayout.CENTER)
        isOpaque = false
    }

    override fun bindData(list: JList<out String>?, value: String?, index: Int) {
        titleLabel.text = value
    }


}

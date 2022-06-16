package ui.setting

import ui.SkinComboSelector
import javax.swing.JScrollPane
import javax.swing.JTextField

class ThemePanel : OptionPanel() {

    init {

        add(
            JScrollPane(getSettingPanel(
            Option("테마", SkinComboSelector())
        ))
        )

    }

}
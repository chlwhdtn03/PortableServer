package ui.setting

import data.Option
import ui.SkinComboSelector
import javax.swing.JScrollPane

class ThemePanel : OptionPanel() {

    init {

        add(
            JScrollPane(getSettingPanel(
            Option("테마", SkinComboSelector())
        ))
        )

    }

}
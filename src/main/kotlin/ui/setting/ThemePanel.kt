package ui.setting

import data.Option
import ui.component.SkinComboSelector
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
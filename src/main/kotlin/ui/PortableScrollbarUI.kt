package ui

import java.awt.Color
import java.awt.Dimension

import javax.swing.JButton
import javax.swing.plaf.basic.BasicScrollBarUI


class PortableScrollbarUI : BasicScrollBarUI() {
    private fun createZeroButton(): JButton? {
        val button = JButton()
        button.preferredSize = Dimension(0, 0)
        button.minimumSize = Dimension(0, 0)
        button.maximumSize = Dimension(0, 0)
        return button
    }

    protected override fun createDecreaseButton(orientation: Int): JButton? {
        return createZeroButton()
    }

    protected override fun createIncreaseButton(orientation: Int): JButton? {
        return createZeroButton()
    }

    protected override fun configureScrollBarColors() {
        this.thumbColor = Color(200, 200, 200, 50)
        this.minimumThumbSize = Dimension(0, 50)
        this.maximumThumbSize = Dimension(0, 50)
        this.thumbDarkShadowColor = Color(200, 200, 200)
    }
}
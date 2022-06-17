package ui

import data.PointerBoolean
import java.awt.Color
import java.awt.Graphics
import javax.swing.JToggleButton

class PortableToggle(private val target : PointerBoolean = PointerBoolean(false)) : JToggleButton() {
    init {
        addActionListener {
            target.b = !target.b
        }
        isFocusable = false
    }

    override fun paintComponents(g: Graphics) {
        g.clearRect(0,0, width, height)
        if(target.b) {
            g.color = Color.green
            g.fillRect(width/2, 0, width, height)
        } else {
            g.color = Color.red
            g.fillRect(0, 0, width/2, height)
        }
    }

}
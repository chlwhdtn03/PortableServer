package ui

import data.PointerBoolean
import org.pushingpixels.radiance.theming.api.ComponentState
import org.pushingpixels.radiance.theming.api.RadianceThemingCortex
import org.pushingpixels.radiance.theming.api.RadianceThemingSlices
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.SystemColor
import javax.swing.JToggleButton

class PortableToggle(private val target : PointerBoolean = PointerBoolean(false)) : JToggleButton("AAAA") {
    init {
        addActionListener {
            target.b = !target.b
        }
        isFocusable = false
    }

    override fun paintComponent(g: Graphics?) {
        println(width)
        if(g != null) {
            g.clearRect(0,0, width, height)
            if(target.b) {
                g.color = Color(120,200,200)
                g.fillRect(width/2, 0, width, height)
            } else {
                g.color = Color(200,50,50)
                g.fillRect(0, 0, width/2, height)
            }
        }
    }
}
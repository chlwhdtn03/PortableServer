package ui

import javafx.scene.control.TextArea
import javafx.scene.control.skin.TextAreaSkin
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.swing.JTextArea
import javax.swing.SwingUtilities

class BootstrapPrintStream(private val consoleArea: TextArea) : OutputStream() {
    override fun write(b: Int) {
        consoleArea.appendText("${b.toChar()}")
    }

    override fun write(b: ByteArray) {
        write(b, 0, b.size)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        consoleArea.appendText("${String(b, off, len)}")
    }



}

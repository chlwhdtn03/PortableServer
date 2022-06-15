package ui

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import javax.swing.JTextArea
import javax.swing.SwingUtilities

class BootstrapPrintStream(private val consoleArea: JTextArea) : OutputStream() {
    override fun write(b: Int) {
        consoleArea.append("${b.toChar()}")
        consoleArea.caretPosition = consoleArea.document.length
    }

    override fun write(b: ByteArray) {
        write(b, 0, b.size)
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        consoleArea.append("${String(b, off, len)}")
        consoleArea.caretPosition = consoleArea.document.length
    }



}

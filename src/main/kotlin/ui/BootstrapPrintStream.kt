package ui

import java.io.OutputStream
import javax.swing.JTextArea

class BootstrapPrintStream(private val consoleArea: JTextArea) : OutputStream() {

    override fun write(b: Int) {
        consoleArea.append("${b.toChar()}")
        consoleArea.caretPosition = consoleArea.document.length
    }

}

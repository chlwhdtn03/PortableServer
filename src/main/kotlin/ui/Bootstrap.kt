package ui

import tornadofx.*
import java.io.PrintStream

class Bootstrap : App(PortableMain::class, PortableStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}
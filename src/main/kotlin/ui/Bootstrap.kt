package ui

import tornadofx.*

class Bootstrap : App(PortableMain::class, PortableStyle::class) {
    init {
        reloadStylesheetsOnFocus()
    }
}
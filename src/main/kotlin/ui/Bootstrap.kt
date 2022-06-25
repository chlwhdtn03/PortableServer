package ui

import data.PortableObject
import data.RouterObject
import file.FileManager
import javafx.scene.Parent
import server.PortableServer
import tornadofx.View
import tornadofx.button
import tornadofx.vbox
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.PrintStream
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException
import java.util.*
import javax.swing.*
import javax.swing.border.TitledBorder
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel


class Bootstrap(private val VERSION: String) : View() {
    override val root = vbox {
        button {
            text = "Hello World"
        }
    }
}
package ui

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
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


class Bootstrap(private val VERSION: String) : JFrame() {

    val address_label = JLabel("IP가 존재하지 않습니다")
    private val addresslist: MutableList<String> = ArrayList()
    private var address_count = 0

    val consolePanel = JPanel()
    val console_input = JTextField()
    val console_area = JTextArea()
    val console_scroll = JScrollPane(console_area)

    val listenerPanel = JPanel()
    val listener_list = JList<String>()
    val listener_addBtn = JButton()
    val listener_scroll = JScrollPane(listener_list)

    val visitorPanel = JPanel()
    val visitor_list = JList<String>()
    val visitor_scroll = JScrollPane(visitor_list)

    init {
        title = "PortableServer $VERSION"
        size = Dimension(600,500)

        setLocationRelativeTo(null)

        defaultCloseOperation = EXIT_ON_CLOSE

        layout = BorderLayout()

        initConsoleComponent(this)
        initListenerComponent(this)
        initVisitorComponent(this)

        isVisible = true

        settingComponentSize()

        println("GUI is activated")
    }

    private fun settingComponentSize() {
        consolePanel.preferredSize = consolePanel.size
        visitorPanel.preferredSize = visitorPanel.size
    }

    private fun initConsoleComponent(frame: JFrame) {
        consolePanel.border = TitledBorder("▶ RUN")
        consolePanel.layout = BorderLayout()

        console_scroll.verticalScrollBar.setUI(PortableScrollbarUI())

        /**
         *  모든 출력로그를 JTextArea에 출력
         */
        val ps = PrintStream(BootstrapPrintStream(console_area))
        System.setErr(ps)
        System.setOut(ps)

        console_area.isEditable = false

        consolePanel.add(console_scroll, BorderLayout.CENTER)
        consolePanel.add(console_input, BorderLayout.SOUTH)

        println("Console UI Loaded")
        frame.add(consolePanel, BorderLayout.SOUTH)

    }

    private fun initListenerComponent(frame: JFrame) {
        checkIP()
        address_label.font = Font("맑은 고딕", Font.PLAIN, 14)

        listenerPanel.border = TitledBorder("Server")
        listenerPanel.layout = BorderLayout()

        listener_addBtn.text = "Add Router"
        listener_addBtn.font = Font("맑은 고딕", Font.PLAIN, 14)

        listener_scroll.verticalScrollBar.setUI(PortableScrollbarUI())

        listenerPanel.add(listener_scroll, BorderLayout.CENTER)
        listenerPanel.add(listener_addBtn, BorderLayout.SOUTH)
        listenerPanel.add(address_label, BorderLayout.NORTH)

        println("Listener UI Loaded")
        frame.add(listenerPanel, BorderLayout.CENTER)
    }

    private fun initVisitorComponent(frame: JFrame) {


        visitorPanel.border = TitledBorder("Visitor")

        visitorPanel.layout = BorderLayout()

        visitor_scroll.verticalScrollBar.setUI(PortableScrollbarUI())


        visitorPanel.add(visitor_scroll, BorderLayout.CENTER)

        println("Visitor UI Loaded")
        frame.add(visitorPanel, BorderLayout.EAST)
    }

    private fun checkIP() {
        try {
            address_label.text ="Address : " + InetAddress.getLocalHost().hostAddress + " (Click to change if exist)"
            println("Checking IP Addresses...")
            val n: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            var e: NetworkInterface
            var a: Enumeration<InetAddress>
            var addr: InetAddress
            while (n.hasMoreElements()) {
                e = n.nextElement()
                if (e.isLoopback) {
                    // 루프백 주소 필요 X
                    continue
                }
                if (e.isVirtual) {
                    // 가상환경 주소 필요 X
                    continue
                }
                a = e.inetAddresses
                while (a.hasMoreElements()) {
                    addr = a.nextElement()
                    if (addr.isLinkLocalAddress) {
                        continue
                    }
                    println(e.displayName + " - " + addr.hostAddress)
                    addresslist.add(addr.hostAddress)
                }
            }
//            address_label.setFont(Font("맑은 고딕", Font.BOLD, 22))
        } catch (e: UnknownHostException) {
            println(e)
        } catch (e: SocketException) {
            println(e)
        }
        address_label.horizontalAlignment = SwingUtilities.CENTER

        address_label.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (address_count >= addresslist.size) address_count = 0
                (e.source as JLabel).text = "Address : " + addresslist[address_count++] + ""
            }
        })
    }

}
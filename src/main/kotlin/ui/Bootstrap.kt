package ui

import data.PortableObject
import data.RouterObject
import file.FileManager
import server.PortableServer
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


class Bootstrap(private val VERSION: String) : PortableFrame() {
    val objects: MutableList<PortableObject> = ArrayList<PortableObject>()
    val routers: MutableList<RouterObject> = ArrayList<RouterObject>()

    private val addresslist: MutableList<String> = ArrayList()
    private var address_count = 0

    val address_label = JLabel("IP가 존재하지 않습니다")

    val consolePanel = JPanel()
    val console_input = JTextField()
    val console_area = JTextArea()
    val console_scroll = JScrollPane(console_area)

    val treeNode = DefaultMutableTreeNode("ROOT")

    val listenerPanel = JPanel()

    val listenerInnerPanel = JPanel()
    val listener_tree = JTree(treeNode)
    val listener_addBtn = JButton()
    val listener_scroll = JScrollPane(listener_tree)

    val object_list = JList<String>()
    val object_addBtn = JButton()
    val object_scroll = JScrollPane(object_list)

    val visitorPanel = JPanel()
    val visitor_area = JList<String>()
    val visitor_scroll = JScrollPane(visitor_area)
    var visitor_list = emptyArray<String>()

    val listener_leftpanel = JPanel()
    val listener_rightpanel = JPanel()


    init {
        setAllFont()
        title = "PortableServer $VERSION"
        size = Dimension(800,500)

        setLocationRelativeTo(null)

        defaultCloseOperation = EXIT_ON_CLOSE

        layout = BorderLayout()

        initConsoleComponent(this)
        initListenerComponent(this)
        initVisitorComponent(this)

        val menu_bar = JMenuBar()
        val menu_1 = JMenu("설정")
        val menu_item_cert = JMenuItem("인증서 설정")
        menu_bar.add(menu_1)
        menu_1.add(menu_item_cert)
        menu_item_cert.addActionListener {
            SettingGUI()
        }

        jMenuBar = menu_bar

        isVisible = true

        settingComponentSize()
        settingComponentListener()

        println("GUI is activated")

        object_list.setListData(loadPortableObjectList())
        loadRouterList()
    }

    public fun loadPortableObjectList() : Array<String> {
        val results = FileManager.loadObjects()

        objects.clear()

        for(i in results.indices) {
            results[i] = results[i].split(".")[0]
            objects.add(FileManager.loadObject(results[i]))
        }
        return results
    }

    public fun loadRouterList() {
        val results = FileManager.loadRouters()
        treeNode.removeAllChildren()

        PortableServer.resetRouter()

        var temp: RouterObject
        for(i in results.indices) {
            temp = FileManager.loadRouter(results[i].split(".")[0])
            treeNode.insert(DefaultMutableTreeNode("${temp.name} (/${temp.address}) - ${temp.type}"), i)
            PortableServer.addRoute(temp)
        }
        (listener_tree.model as DefaultTreeModel).reload(treeNode)
    }

    public fun recordVisitor(visitor: String, routername: String, status: String) {
        visitor_list = visitor_list.plus("$visitor [$routername]\n $status")
        visitor_area.setListData(visitor_list)
        visitor_area.selectedIndex = visitor_list.indices.last
    }

    private fun settingComponentListener() {
        object_addBtn.addActionListener {
            ModifyObjectGUI(this, "New Object")
        }

        listener_addBtn.addActionListener {
            ModifyRouterGUI(this, "New Router")
        }
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
         *  == 모든 출력로그를 JTextArea에 출력 ==
         */
        val ps = PrintStream(BootstrapPrintStream(console_area), true)
        System.setErr(ps)
        System.setOut(ps)
        println("PrintStream Loaded")

        /**
         * ====
         */

        console_area.isEditable = false

        consolePanel.add(console_scroll, BorderLayout.CENTER)
        consolePanel.add(console_input, BorderLayout.SOUTH)

        println("Console UI Loaded")
        frame.add(consolePanel, BorderLayout.SOUTH)

    }

    private fun initListenerComponent(frame: JFrame) {
        checkIP()

        listener_tree.isEditable = false
        listener_tree.dragEnabled = false

        listenerPanel.border = TitledBorder("Server")
        listenerPanel.layout = BorderLayout()

        listener_addBtn.text = "Add Router"

        listener_scroll.verticalScrollBar.setUI(PortableScrollbarUI())


        val listener_popup = JPopupMenu()
        val listener_item_modify = JMenuItem("Modify")
        listener_item_modify.addActionListener {
            ModifyRouterGUI(
                this,
                FileManager.loadRouter(
                    (listener_tree.lastSelectedPathComponent as DefaultMutableTreeNode).userObject.toString()
                        .split(" (")[0].trim()
                )
            )
        }
        val listener_item_delete = JMenuItem("Delete")
        listener_item_delete.addActionListener {
            FileManager.deleteRouter(this, (listener_tree.lastSelectedPathComponent as DefaultMutableTreeNode).userObject.toString().split(" (")[0].trim())
        }
        listener_popup.add(listener_item_modify)
        listener_popup.add(listener_item_delete)

        listener_tree.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    val slot: Int = listener_tree.getRowForLocation(e.x, e.y)
                    if (slot < 1) return
                    listener_tree.setSelectionRow(slot)
                    listener_popup.show(listener_tree, e.x, e.y)
                    super.mouseClicked(e)
                }
            }
        })

        object_scroll.verticalScrollBar.setUI(PortableScrollbarUI())
        object_addBtn.text = "Add Object"

        val object_popup = JPopupMenu()
        val item_modify = JMenuItem("Modify")
        item_modify.addActionListener {
            ModifyObjectGUI(this, objects[object_list.selectedIndex])
        }
        val item_delete = JMenuItem("Delete")
        item_delete.addActionListener {
            FileManager.deleteObject(this, objects[object_list.selectedIndex].name)
        }
        object_popup.add(item_modify)
        object_popup.add(item_delete)

        object_list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    val slot: Int = object_list.locationToIndex(e.point)
                    if (slot == -1) return
                    object_list.selectedIndex = slot
                    object_popup.show(object_list, e.x, e.y)
                    super.mouseClicked(e)
                }
            }
        })

        listenerInnerPanel.layout = BorderLayout()

        listener_rightpanel.layout = BorderLayout()
        listener_rightpanel.add(listener_scroll, BorderLayout.CENTER)
        listener_rightpanel.add(listener_addBtn, BorderLayout.SOUTH)

        listener_leftpanel.layout = BorderLayout()
        listener_leftpanel.add(object_scroll, BorderLayout.CENTER)
        listener_leftpanel.add(object_addBtn, BorderLayout.SOUTH)

        listenerInnerPanel.add(listener_rightpanel, BorderLayout.CENTER)
        listenerInnerPanel.add(listener_leftpanel, BorderLayout.EAST)

        listenerPanel.add(listenerInnerPanel, BorderLayout.CENTER)
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

    private fun setAllFont() {
//        var font = Font("맑은 고딕",Font.PLAIN,14)
//        UIManager.getLookAndFeelDefaults()["defaultFont"] = Font("맑은 고딕", Font.BOLD, 14)
    }

}
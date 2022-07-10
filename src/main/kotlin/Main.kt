import server.PortableServer
import tornadofx.launch
import tornadofx.runLater
import ui.Bootstrap
import ui.BootstrapPrintStream
import ui.PortableComponent
import util.OptionManager
import java.io.PrintStream
import javax.swing.JFrame
import javax.swing.SwingUtilities

private const val VERSION: String = "1.1 SNAPSHOT"
private const val port: Int = 80

fun main(args: Array<String>) {
        // TODO : GUI 불가능한 환경에선 에러날거임

        OptionManager.loadOptions()
        val uiThread: Thread = Thread {
                launch<Bootstrap>(args)
        }
        uiThread.name = "GUI Thread"
        uiThread.isDaemon = true
        uiThread.start()
        println("Initing Server...")

//                if (bootstrap.isVisible) {
//                        println("GUI 활성화")
//                    PortableServer(VERSION, port, bootstrap)
//                } else {
//                        println("GUI 실패")
//                    PortableServer(VERSION, port)
//                }

        PortableServer(VERSION, port)

}
import server.PortableServer
import ui.Bootstrap
import java.util.logging.Logger
import javax.swing.JFrame
import javax.swing.SwingUtilities

private val VERSION: String = "0.1 SNAPSHOT"
private val port: Int = 80

fun main(args: Array<String>) {
        JFrame.setDefaultLookAndFeelDecorated(true)
        // TODO : GUI 불가능한 환경에선 에러날거임
        SwingUtilities.invokeLater {
                val bootstrap = Bootstrap(VERSION)

                println("Checking HTTPS key...")
                println("Initing HTTP Server...")

                if(bootstrap.isVisible) {
                        println("GUI 활성화")
                        PortableServer(VERSION, port, bootstrap)
                } else {
                        println("GUI 실패")
                        PortableServer(VERSION, port)
                }
        }
}
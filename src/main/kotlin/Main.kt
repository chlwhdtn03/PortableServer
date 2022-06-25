import server.PortableServer
import ui.Bootstrap
import util.OptionManager
import javax.swing.JFrame
import javax.swing.SwingUtilities

private const val VERSION: String = "1.1 SNAPSHOT"
private const val port: Int = 80

fun main(args: Array<String>) {
        JFrame.setDefaultLookAndFeelDecorated(true)
        // TODO : GUI 불가능한 환경에선 에러날거임
        SwingUtilities.invokeLater {
                OptionManager.loadOptions()
                val bootstrap = Bootstrap(VERSION)


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
}


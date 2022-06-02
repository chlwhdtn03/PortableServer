import server.PortableServer
import ui.Bootstrap
import java.util.logging.Logger

private val VERSION: String = "0.1 SNAPSHOT"
private val port: Int = 80

fun main(args: Array<String>) {

        // TODO : GUI 불가능한 환경에선 에러날거임
        val bootstrap = Bootstrap(VERSION)

        println("Checking HTTPS key")
        println("Initing HTTP Server...")

        if(bootstrap.isActive) {
                println("GUI 활성화")
                PortableServer(VERSION, port, bootstrap)
        } else {
                println("GUI 실패")
                PortableServer(VERSION, port)
        }

}
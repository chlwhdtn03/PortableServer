import server.PortableServer
import ui.Bootstrap
import java.util.logging.Logger

private val VERSION: String = "0.1 SNAPSHOT"
private val port: Int = 80

fun main(args: Array<String>) {

        // TODO : GUI 불가능한 환경에선 에러날거임
        Bootstrap(VERSION)

        println("Checking HTTPS key")
        println("Initing HTTP Server...")

        PortableServer(VERSION, port)

}
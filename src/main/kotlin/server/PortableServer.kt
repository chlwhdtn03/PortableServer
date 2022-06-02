package server

import data.PortableObject
import data.RouterType
import file.FileManager
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Router
import ui.Bootstrap

class PortableServer(VERSION: String, PORT: Int) {
    companion object {
        val vertx: Vertx = Vertx.vertx()
        val server:HttpServer = vertx.createHttpServer()
        val router:Router = Router.router(vertx)

        fun addRoute(path: String, target_object: PortableObject, type: RouterType) {
            if(type == RouterType.GET || type == RouterType.GET_POST) {
                router.get(path).handler { requesthandler ->
                    val response = requesthandler.response()
                    response.putHeader("content-type","text/plain")
                    response.end("GET for $target_object")
                }
            }
            if(type == RouterType.POST || type == RouterType.GET_POST) {
                router.post(path).handler { requesthandler ->
                    val response = requesthandler.response()
                    response.putHeader("content-type","text/plain")
                    response.end("POST for $target_object")
                }
            }
        }
    }
    init {
        router.route("/").handler { requesthandler ->
            val response = requesthandler.response()
            response.putHeader("content-type", "text/plain")
            response.end("Opened by PortableServer $VERSION !")
        }

        router.route("/admin").handler { requesthandler ->
            val response = requesthandler.response()
            response.putHeader("content-type", "text/plain")
            response.end("""
                OS : ${System.getProperty("os.name")} ${System.getProperty("os.version")}
                Architecture : ${System.getProperty("os.arch")}
                JRE : ${System.getProperty("java.version")}
            """.trimIndent())
        }

        server.requestHandler(router).listen(PORT).onComplete {
            println("Server Opened in port $PORT")
            println("URL : http://localhost/")
        }
    }



}
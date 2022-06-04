package server

import data.PortableObject
import data.RouterObject
import data.RouterType
import file.FileManager
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler
import ui.Bootstrap
import java.nio.charset.StandardCharsets

class PortableServer(VERSION: String, PORT: Int) {
    var bootstrap: Bootstrap? = null
    constructor(VERSION: String, PORT: Int, bootstrap: Bootstrap) : this(VERSION, PORT) {
        this.bootstrap = bootstrap
    }

    companion object {
        val vertx: Vertx = Vertx.vertx()
        val server:HttpServer = vertx.createHttpServer()
        val router:Router = Router.router(vertx)

        fun addRoute(routerObject: RouterObject) {
            if(routerObject.type == RouterType.GET || routerObject.type  == RouterType.GET_POST) {
                router.get("/" + routerObject.address).setName("custom").handler { requesthandler ->
                    val response = requesthandler.response()
                    response.putHeader("content-type","text/plain;charset=utf-8")
                    if(routerObject.target_object == null)
                        response.end(routerObject.message)
                    else
                        response.end("GET for ${routerObject.target_object}")
                }
            }
            if(routerObject.type == RouterType.POST || routerObject.type == RouterType.GET_POST) {
                router.post("/" + routerObject.address).setName("custom").handler { requesthandler ->
                    val response = requesthandler.response()
                    response.putHeader("content-type","text/plain;charset=utf-8")
                    if(routerObject.target_object == null)
                        response.end(routerObject.message)
                    else
                        response.end("POST for ${routerObject.target_object}")
                }
            }
        }

        fun resetRouter() {
            router.routes.onEach {
                println(it.name)
                if(it.name == "custom")
                    it.remove()
            }
            println("All of Router had reset")
        }
    }
    init {


        router.route().order(0).handler {
            recordVisitor(it)
            it.next() // 다음 핸들러가 존재할 경우 넘어가는 코드
        }

        router.get("/").handler { requesthandler ->
            val response = requesthandler.response()
            response.putHeader("content-type", "text/plain")
            response.end("Opened by PortableServer $VERSION !")
        }


        router.get("/admin").handler { requesthandler ->
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

    fun recordVisitor(request: RoutingContext) {
        bootstrap?.recordVisitor(request.request().remoteAddress().hostAddress(), request.request().absoluteURI())
    }



}
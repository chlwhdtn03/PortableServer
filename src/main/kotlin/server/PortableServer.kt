package server

import data.PortableObject
import data.RouterObject
import data.RouterMethod
import data.TriggerType
import file.FileManager
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.buffer.appendJson
import kotlinx.serialization.json.JsonObject
import ui.Bootstrap

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

            val route = router.route("/"+routerObject.address)
            when(routerObject.type) {
                RouterMethod.GET -> {
                    route.method(HttpMethod.GET)
                }
                RouterMethod.POST -> {
                    route.method(HttpMethod.POST)
                }
                RouterMethod.GET_POST -> {
                    route.method(HttpMethod.POST).method(HttpMethod.GET)
                }
                RouterMethod.UPDATE -> {
                    route.method(HttpMethod.UPDATE)
                }
                RouterMethod.DELETE -> {
                    route.method(HttpMethod.UPDATE)
                }
            }
            route.setName("custom").handler { requesthandler ->
                val response = requesthandler.response()
                response.putHeader("content-type","text/plain;charset=utf-8")
                if(routerObject.target_object == null) {
                    response.end(routerObject.message.ifEmpty { "라우터 설정에서 메세지를 추가하실 수 있습니다." })
                } else {
                    val request = requesthandler.request()
                    when (routerObject.target_trigger) {
                            TriggerType.ADD_DATA -> {
                                var now_varname = ""
                                var temp_str = ""
                                var primary_key = ""
                                var data = io.vertx.core.json.JsonObject()

                                for(i in routerObject.target_object.varNames.indices) {
                                    now_varname = routerObject.target_object.varNames[i]
                                    if(now_varname.isEmpty())
                                        continue


                                    temp_str = request.getParam(now_varname, "")

                                    if(i == 0)
                                        primary_key = temp_str

                                    if(temp_str.isEmpty()) {
                                        response.statusCode = 412
                                        response.end("$now_varname 필드가 비어있습니다.")
                                        return@handler
                                    }
                                    data.put(now_varname, temp_str)

                                }
                                if(FileManager.addRequestObject(routerObject.target_object.name, primary_key, data.toString())) {
                                    response.statusCode = 200
                                    response.end("데이터를 성공적으로 추가하였습니다.")
                                } else {
                                    response.statusCode = 403
                                    response.end("이미 존재하는 데이터입니다. 일반키가 겹치는지 확인하세요")
                                }
                            }
                            TriggerType.GET_DATA -> {
                                var primary_key = request.getParam(routerObject.target_object.varNames[0], "")
                                if(primary_key.isEmpty()) {
                                    response.statusCode = 412
                                    response.end(routerObject.target_object.varNames[0] + " 필드가 비어있습니다.")
                                    return@handler
                                }

                                var result:String = FileManager.getRequestObject(routerObject.target_object.name, primary_key)
                                if(result.trim().isNotEmpty()) {
                                    val json = io.vertx.core.json.JsonObject(result)
                                    for(i in routerObject.target_object.varProvide.indices) {
                                       if(!routerObject.target_object.varProvide[i]) {
                                           json.remove(routerObject.target_object.varNames[i])
                                       }
                                    }
                                    response.statusCode = 200
                                    response.end(json.toString())
                                } else {
                                    response.statusCode = 403
                                    response.end("존재하지 않는 데이터입니다.")
                                }
                            }
                            TriggerType.CHECK_DATA_BY_PRIMARY_KEY -> {


                            }
                            TriggerType.MODIFY_DATA_BY_PRIMARY_KEY -> {


                            }
                            TriggerType.REMOVE_DATA -> {


                            }
                            null -> TODO()
                        }
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

        router.route().order(0).handler(BodyHandler.create().setBodyLimit(500)).handler {


            recordVisitor(it)
            it.next() // 다음 핸들러가 존재할 경우 넘어가는 코드
        }.failureHandler {
            recordVisitor(it)
            it.response().isChunked = true
            it.response().putHeader("content-type","text/html;charset=utf-8")
            it.response().write("${it.statusCode()} ERROR\n${errorMessage(it.statusCode())}")

            it.response().end()
        }

        router.get("/").handler { requesthandler ->
            val response = requesthandler.response()
            response.putHeader("content-type", "text/html")
            response.end("Opened by PortableServer $VERSION !<br>Route size:${router.routes.size}")
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
        bootstrap?.recordVisitor(request.request().remoteAddress().hostAddress(), request.request().uri(), if(request.statusCode() == -1) "" else request.statusCode().toString())
    }

    private fun errorMessage(statuscode: Int) : String {
        return when (statuscode) {
            413 -> {
                "너무 큰 요청 사이즈"
            }
            403 -> {
                "금지된 접근"
            }
            401 -> {
                "권한 없음"
            }

            else -> {
                "알 수 없는 원인"
            }
        }
    }


}
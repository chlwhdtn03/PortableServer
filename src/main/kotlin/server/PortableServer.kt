package server

import data.*
import file.FileManager
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.net.JksOptions
import io.vertx.core.net.KeyCertOptions
import io.vertx.core.net.PfxOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ui.Bootstrap
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyFactory
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec
import kotlin.random.Random


class PortableServer(VERSION: String, PORT: Int) {
    var bootstrap: Bootstrap? = null
    constructor(VERSION: String, PORT: Int, bootstrap: Bootstrap) : this(VERSION, PORT) {
        this.bootstrap = bootstrap
    }

    companion object {
        private const val storeName = "tempStore.jks"
        private val storePassword = generateKeyStore(10)
        private const val storeType = "jks"

        val vertx: Vertx = Vertx.vertx()
        val server:HttpServer = vertx.createHttpServer(
//            HttpServerOptions()
//                .setSsl(true)
//                .setPfxKeyCertOptions(
//                    PfxOptions()
//                        .setPath("tempStore.pkcs12")
//                        .setPassword("123011")
//                        .setAlias("portableserver")
//                        .setAliasPassword("123011")
//                )
//                .setPfxTrustOptions(
//                    PfxOptions()
//                        .setPath("tempTrust.pkcs12")
//                        .setPassword("123011")
//                        .setAlias("portabletrust")
//                        .setAliasPassword("123011")
//                )
        )
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
                RouterMethod.DELETE -> {
                    route.method(HttpMethod.DELETE)
                }
                RouterMethod.PUT -> {
                    route.method(HttpMethod.PUT)
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
                                var primary_key = request.getParam(routerObject.target_object.varNames[0], "")
                                if(primary_key.isEmpty()) {
                                    response.statusCode = 412
                                    response.end(routerObject.target_object.varNames[0] + " 필드가 비어있습니다.")
                                    return@handler
                                }

                                var input_data = request.getParam(routerObject.target_data, "")
                                if(input_data.isEmpty()) {
                                    response.statusCode = 412
                                    response.end("${routerObject.target_data} 필드가 비어있습니다.")
                                    return@handler
                                }
                                var target:String = FileManager.getRequestObject(routerObject.target_object.name, primary_key)
                                if(target.trim().isNotEmpty()) {

                                    val json = io.vertx.core.json.JsonObject(target)
                                    var check_data = json.getValue(routerObject.target_data)
                                    if(check_data.toString() == input_data.toString()) {

                                        response.statusCode = 200
                                        response.end(Json.encodeToString(PortableResponse(200, true, "값이 일치합니다.")))
                                    } else {

                                        response.statusCode = 200
                                        response.end(Json.encodeToString(PortableResponse(200, false, "값이 일치하지 않습니다.")))
                                    }

                                } else {
                                    response.statusCode = 200
                                    response.end(Json.encodeToString(PortableResponse(200, false, "값이 일치하지 않습니다.")))
                                }

                            }
                            TriggerType.MODIFY_DATA_BY_PRIMARY_KEY -> {
                                var primary_key = request.getParam(routerObject.target_object.varNames[0], "")
                                if(primary_key.isEmpty()) {
                                    response.statusCode = 412
                                    response.end(routerObject.target_object.varNames[0] + " 필드가 비어있습니다.")
                                    return@handler
                                }
                                var target:String = FileManager.getRequestObject(routerObject.target_object.name, primary_key)
                                if(target.isEmpty()) {
                                    response.statusCode = 403
                                    response.end("존재하지 않는 데이터입니다.")
                                    return@handler
                                }
                                val json = io.vertx.core.json.JsonObject(target)

                                var changed: Array<String> = emptyArray()
                                for(i in 1 until routerObject.target_object.varNames.size) {
                                    var input_data = request.getParam(routerObject.target_object.varNames[i], "")
                                    if(input_data.isEmpty())
                                        continue
                                    json.remove(routerObject.target_object.varNames[i])
                                    json.put(routerObject.target_object.varNames[i], input_data)
                                    changed = changed.plus(routerObject.target_object.varNames[i])
                                }
                                if(changed.isEmpty()) {
                                    response.statusCode = 200
                                    response.end(Json.encodeToString(PortableResponse(200, false, "변경된 값이 없습니다")))
                                } else {
                                    response.statusCode = 200
                                    FileManager.modifyRequestObject(routerObject.target_object.name, primary_key, json.toString())
                                    response.end(Json.encodeToString(PortableResponse(200, true, changed.joinToString(",") + " 변경됨")))
                                }


                            }
                            TriggerType.REMOVE_DATA -> {
                                var primary_key = request.getParam(routerObject.target_object.varNames[0], "")
                                if(primary_key.isEmpty()) {
                                    response.statusCode = 412
                                    response.end(routerObject.target_object.varNames[0] + " 필드가 비어있습니다.")
                                    return@handler
                                }
                                if(FileManager.removeRequestObject(routerObject.target_object.name, primary_key)) {
                                    response.statusCode = 200
                                    response.end(Json.encodeToString(PortableResponse(200, true, "삭제 되었습니다.")))
                                } else {
                                    response.statusCode = 200
                                    response.end(Json.encodeToString(PortableResponse(200, false, "삭제되지 못했습니다.")))
                                }

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

        private fun generateKeyStore(n: Int): String {
            val characterSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@^"

            val random = Random(System.nanoTime())
            val password = StringBuilder()

            for (i in 0 until n) {
                val rIndex = random.nextInt(characterSet.length)
                password.append(characterSet[rIndex])
            }

            println("Creating HTTPS key...")
            val fileOutputStream = FileOutputStream(storeName)
            val fileInputStream = FileInputStream(storeName)
            val keystore = KeyStore.getInstance(storeType)
//            keystore.load(null, password.toString().toCharArray())
//            keystore.store(fileOutputStream, password.toString().toCharArray())
//            keystore.load(fileInputStream, password.toString().toCharArray())
//            println("Created HTTPS key : ${password.toString()}")

//            val fis = FileInputStream("pkey.der")
//            val dis = DataInputStream(fis)
//            val bytes = ByteArray(dis.available())
//            dis.readFully(bytes)
//            val bais = ByteArrayInputStream(bytes)
//
//            val key = ByteArray(bais.available())
//            val kf: KeyFactory = KeyFactory.getInstance("RSA")
//            bais.read(key, 0, bais.available())
//            bais.close()
//
//            val keysp = PKCS8EncodedKeySpec(key)
//            val ff: PrivateKey = kf.generatePrivate(keysp)
//
//
//            // read the certificates from the files and load them into the key store:
//            val col_crt1 = CertificateFactory.getInstance("X509").generateCertificates(FileInputStream("cert1.pem"))
//            val col_crt2 = CertificateFactory.getInstance("X509").generateCertificates(FileInputStream("cert2.pem"))
//
//            val crt1: Certificate = col_crt1.iterator().next() as Certificate
//            val crt2: Certificate = col_crt2.iterator().next() as Certificate
//            val chain: Array<Certificate> = arrayOf<Certificate>(crt1, crt2)
//
//            val alias1: String = (crt1 as X509Certificate).subjectX500Principal.name
//            val alias2: String = (crt2 as X509Certificate).subjectX500Principal.name
//
//            keystore.setCertificateEntry(alias1, crt1)
//            keystore.setCertificateEntry(alias2, crt2)
//
//            // store the private key
//            keystore.setKeyEntry("tempAlias", ff, password.toString().toCharArray(), chain)
//
//            // save the key store to a file
//            keystore.store(FileOutputStream(storeName), password.toString().toCharArray())

            return password.toString()
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
            println("Server Opened in port ${PORT}")
            println("URL : https://localhost")
        }.onFailure {
            println(it.message)
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
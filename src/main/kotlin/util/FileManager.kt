package util

import data.PortableObject
import data.RouterObject
import io.vertx.core.json.JsonObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ui.Bootstrap
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter

class FileManager {

    companion object {

        fun saveRouter(routername: String, data: String) {
            val dir: File = File("data/router/")
            if (!dir.isDirectory)
                dir.mkdirs()
            val file: File = File("data/router/${routername}.txt")
            if (!file.exists())
                file.createNewFile()

            println(file.absolutePath)

            val bw = BufferedWriter(FileWriter(file))
            bw.write(data)
            bw.flush()
            bw.close()
        }

        fun loadRouter(routername: String): RouterObject {
            val file: File = File("data/router/${routername}.txt")

            if (!file.exists())
                throw FileNotFoundException("해당 오브젝트 파일이 존재하지 않습니다.")
            val br = BufferedReader(FileReader(file))
            val s = br.readLine()
            br.close()
//            println(s)
            var loaded = Json.decodeFromString<RouterObject>(s)
            return RouterObject(loaded.name, loaded.address, loaded.type, loaded.target_object?.let { loadObject(it.name) }, loaded.target_trigger, loaded.target_data)
        }

        fun loadRouters(): Array<String> {
            val dir: File = File("data/router/")
            if (!dir.isDirectory)
                dir.mkdirs()
            return dir.list() as Array<String>
        }

        fun deleteRouter(bootstrap: Bootstrap, routername: String) {
            val file: File = File("data/router/${routername}.txt")

            if (!file.exists())
                throw FileNotFoundException("해당 라우터 파일이 존재하지 않습니다.")

            if(file.delete())
                println("DELETED $routername")
            else
                println("Failed to delete $routername")
            bootstrap.loadRouterList()

        }

        fun saveObject(objectname: String, data: String) {
            val dir: File = File("data/object/")
            if (!dir.isDirectory)
                dir.mkdirs()
            val file: File = File("data/object/${objectname}.txt")
            if (!file.exists())
                file.createNewFile()

            println(file.absolutePath)

            val bw = BufferedWriter(FileWriter(file))
            bw.write(data)
            bw.flush()
            bw.close()
        }

        fun loadObject(objectname: String): PortableObject {
            val file: File = File("data/object/${objectname}.txt")

            if (!file.exists())
                throw FileNotFoundException("해당 오브젝트 파일이 존재하지 않습니다.")
            val br = BufferedReader(FileReader(file))
            val s = br.readLine()
            br.close()
//            println(s)
            return Json.decodeFromString<PortableObject>(s)
        }

        fun loadObjects(): Array<String> {
            val dir: File = File("data/object/")
            if (!dir.isDirectory)
                dir.mkdirs()
            return dir.list() as Array<String>
        }

        fun deleteObject(bootstrap: Bootstrap, objectname: String) {
            val file: File = File("data/object/${objectname}.txt")

            if (!file.exists())
                throw FileNotFoundException("해당 오브젝트 파일이 존재하지 않습니다.")

            if(file.delete())
                println("DELETED $objectname")
            else
                println("Failed to delete $objectname")
            bootstrap.loadPortableObjectList()

        }

        /**
         * @return 이미 존재하는 일반키이면 false, 잘 추가 됬으면 true
         */
        fun addRequestObject(target: PortableObject, data: JsonObject): Boolean {
            var parsered = StringBuffer()
            var cache_count: Int = 0
            for(i in target.varNames.indices) {
                parsered.append("${target.varNames[i]} ${target.varTypes[i]}")
                if(i == 0) {
                    parsered.append(" PRIMARY KEY")
                }
                cache_count = i
                if(target.varNames[i+1].isNotEmpty()) {
                    parsered.append(", ")
                } else {
                    break;
                }
            }

            println(parsered)

            DatabaseManager.statement.executeUpdate("create table if not exists ${target.name}(${parsered})")

            val list = CharArray(cache_count+1)
            for(i in list.indices) {
                list[i] = '?'
            }

            parsered.delete(0, parsered.length)
            for(i in 0..cache_count) {
                parsered.append("${target.varNames[i]}")
                if(target.varNames[i+1].isNotEmpty()) {
                    parsered.append(", ")
                } else {
                    break;
                }
            }

            println(data.toString())
            val ps = DatabaseManager.connection.prepareStatement("INSERT INTO ${target.name}(${parsered}) VALUES(${list.joinToString(separator = ",")})")
            for(i in 1..cache_count+1) {
                when(target.varTypes[i-1]) {
                    "string" -> {
                        ps.setString(i, data.getString(target.varNames[i-1]))
                    }
                    "integer" -> {
                        ps.setInt(i, data.getString(target.varNames[i-1]).toInt())
                    }
                }
            }
            ps.executeUpdate()
            return true
        }

        fun modifyRequestObject(target: PortableObject, data: JsonObject): Boolean {
            val sb = StringBuilder()
            var primarykey = ""
            for(name in data.fieldNames()) {
                if(name == target.varNames[0]) {
                    primarykey = name
                    println("primarykey $primarykey")
                    continue
                } else {

                    println("else $name")
                    if(target.varNames.any { equals(name) }) // 찾을 수 없는 필드명일 경우
                        return false
                    when(target.varTypes[target.varNames.indexOf(name)]) {
                        "integer" -> {
                            sb.append("$name=${data.getString(name).toInt()}, ")
                        }
                        "string" -> {
                            sb.append("$name='${data.getString(name)}', ")
                        }
                    }
                }

            }

            if(primarykey.isBlank()) // 기본키를 찾지 못함
                return false
            sb.delete(sb.length-2, sb.length) // 맨 마지막은 ','일 테니 이거 지우고 where문 붙여야함
            println("update ${target.name} set $sb WHERE $primarykey='${data.getString(primarykey)}'")
            DatabaseManager.statement.executeUpdate("update ${target.name} set $sb WHERE $primarykey='${data.getString(primarykey)}'")
            return true
        }

        fun removeRequestObject(objectname: String, primarykey: String): Boolean {
            val dir: File = File("data/${objectname}/")
            if (!dir.isDirectory)
                dir.mkdirs()
            val file: File = File("data/${objectname}/${primarykey}.txt")
            if (!file.exists()) {
                return false
            }

            return file.delete()

            return true
        }

        fun getRequestObject(obj: PortableObject, primarykey: String): String {
            val row = DatabaseManager.statement.executeQuery("SELECT * FROM ${obj.name} WHERE ${obj.varNames[0]}='$primarykey'")
            row.next()
            if(row.isClosed) // 결과 없단 얘기
                return ""
            val result = JsonObject()
            for(i in obj.varNames.filter { it.isNotBlank() }.indices) {

                when(obj.varTypes[i]) {
                    "integer" -> {
                        result.put(obj.varNames[i], row.getInt(obj.varNames[i]))
                    }
                    "string" -> {
                        result.put(obj.varNames[i], row.getString(obj.varNames[i]))
                    }
                }
            }
            return result.toString()
        }

    }
}
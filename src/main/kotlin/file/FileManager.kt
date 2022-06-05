package file

import data.PortableObject
import data.RouterObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
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
            println(s)
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
            println(s)
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
        fun addRequestObject(objectname: String, primarykey: String, requestobjectjson: String): Boolean {
            val dir: File = File("data/${objectname}/")
            if (!dir.isDirectory)
                dir.mkdirs()
            val file: File = File("data/${objectname}/${primarykey}.txt")
            if (!file.exists()) {
                file.createNewFile()
            } else {
                return false
            }

            val bw = BufferedWriter(FileWriter(file))
            bw.write(requestobjectjson)
            bw.flush()
            bw.close()
            return true
        }

        fun modifyRequestObject(objectname: String, primarykey: String, requestobjectjson: String): Boolean {
            val dir: File = File("data/${objectname}/")
            if (!dir.isDirectory)
                dir.mkdirs()
            val file: File = File("data/${objectname}/${primarykey}.txt")
            if (!file.exists()) {
                return false
            }

            val bw = BufferedWriter(FileWriter(file))
            bw.write(requestobjectjson)
            bw.flush()
            bw.close()
            return true
        }

        fun getRequestObject(objectname: String, primarykey: String): String {
            val dir: File = File("data/${objectname}/")
            if (!dir.isDirectory)
                dir.mkdirs()
            val file: File = File("data/${objectname}/${primarykey}.txt")
            if (!file.exists()) {
                return ""
            }

            val bw = BufferedReader(FileReader(file))
            val result = bw.readLine()
            bw.close()
            return result
        }

    }
}
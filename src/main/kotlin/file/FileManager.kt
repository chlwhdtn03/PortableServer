package file

import data.PortableObject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter

class FileManager {

    companion object {
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
            println(s)
            return Json.decodeFromString<PortableObject>(s)
        }

        fun loadObjects(): Array<String> {
            val dir: File = File("data/object/")
            if (!dir.isDirectory)
                dir.mkdirs()
            return dir.list() as Array<String>
        }
    }
}
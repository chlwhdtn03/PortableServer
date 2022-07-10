package util

import java.io.*
import java.util.Properties

class OptionManager {
    companion object {



        fun saveOptions() {
            with(OptionObject) {
                val dir: File = File("data/")
                if (!dir.isDirectory)
                    dir.mkdirs()
                val file: File = File("data/option.txt")
                if (!file.exists())
                    file.createNewFile()

                val p = Properties()
                val bw =  BufferedWriter(FileWriter(file))

                p.setProperty("SSL_ENABLE", SSL_ENABLE.toString())
                p.setProperty("SSL_KEYSTORE_LOCATION", SSL_KEYSTORE_LOCATION.toString())
                p.setProperty("SSL_TRUSTSTORE_LOCATION", SSL_TRUSTSTORE_LOCATION.toString())
                p.setProperty("THEME_SKIN", THEME_SKIN.toString())
                p.setProperty("SERVER_PORT", SERVER_PORT.toString())

                p.store(bw, "PortableServer Settings")
                bw.close()
            }

        }

        fun loadOptions() {
            with(OptionObject) {
                val file: File = File("data/option.txt")

                if (!file.exists()) {
                    println("옵션 파일이 생성됩니다. ${file.exists()}")
                    saveOptions()
                }
                val br = BufferedReader(FileReader(file))
                val p = Properties()
                p.load(br)
                SSL_ENABLE = p["SSL_ENABLE"].toString().toBoolean()
                SSL_KEYSTORE_LOCATION = p["SSL_KEYSTORE_LOCATION"].toString()
                SSL_TRUSTSTORE_LOCATION = p["SSL_TRUSTSTORE_LOCATION"].toString()
                THEME_SKIN = p["THEME_SKIN"].toString()
                SERVER_PORT = p["SERVER_PORT"].toString().toInt()
                br.close()
            }

        }

    }
}
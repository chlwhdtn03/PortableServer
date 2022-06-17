package util

class OptionManager {
    companion object {
        var SSL_ENABLE: Boolean = false
        var SSL_KEYSTORE_LOCATION: String = ""
        var SSL_TRUSTSTORE_LOCATION: String = ""
        var THEME_SKIN: String = ""
        var SERVER_PORT: Int = 80
    }
}
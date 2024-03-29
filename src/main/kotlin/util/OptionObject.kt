package util

import org.pushingpixels.radiance.theming.api.RadianceThemingCortex

object OptionObject {
    var SSL_ENABLE: Boolean = false
    var SSL_KEYSTORE_LOCATION: String = ""
    var SSL_TRUSTSTORE_LOCATION: String = ""
    var THEME_SKIN: String = RadianceThemingCortex.GlobalScope.getAllSkins().values.random().className
    var SERVER_PORT: Int = 80
}
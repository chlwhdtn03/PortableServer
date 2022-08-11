package util

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object DatabaseManager {
    val connection: Connection by lazy {
        DriverManager.getConnection("jdbc:sqlite:data/core.db")
    }

    val statement: Statement by lazy {
        connection.createStatement().apply {
            queryTimeout = 10 // 10초 제한 Timeout
        }
    }

}
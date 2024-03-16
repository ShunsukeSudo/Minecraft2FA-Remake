package com.github.shunsukesudo.minecraft2fa.shared.database

object DatabaseFactory {
    object SQLite {
        fun newConnection(address: String, databaseName: String, user: String, password: String): MC2FADatabase {
            return SQLiteDatabase(address, databaseName, user, password)
        }
    }

    object MySQL {
        fun newConnection(address: String, databaseName: String, user: String, password: String): MC2FADatabase {
            return MySQLDatabase(address, databaseName, user, password)
        }
    }
}
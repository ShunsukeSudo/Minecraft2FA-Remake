package com.github.shunsukesudo.minecraft2fa.shared.database

object DatabaseFactory {
    object SQLite {
        fun newConnection(dbName: String): MC2FADatabase {
            return SQLiteDatabase(dbName)
        }
    }

    object MySQL {
        fun newConnection(address: String, databaseName: String, properties: String, user: String, password: String): MC2FADatabase {
            return MySQLDatabase(address, databaseName, properties, user, password)
        }
        fun newConnection(address: String, databaseName: String, user: String, password: String): MC2FADatabase {
            return MySQLDatabase(address, databaseName, "", user, password)
        }
    }
}
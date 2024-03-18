package com.github.shunsukesudo.minecraft2fa.shared.database

object DatabaseFactory {
    object SQLite {
        fun newConnection(databaseName: String): MC2FADatabase {
            if(!databaseName.endsWith(".db"))
                return SQLiteDatabase(databaseName.plus(".db"))

            return SQLiteDatabase(databaseName)
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
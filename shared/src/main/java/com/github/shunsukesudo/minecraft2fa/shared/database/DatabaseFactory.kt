package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseConfiguration

object DatabaseFactory {
    object SQLite {
        fun newConnection(databaseConfiguration: DatabaseConfiguration): MC2FADatabase {
            return SQLiteDatabase(databaseConfiguration)
        }
    }

    object MySQL {
        fun newConnection(databaseConfiguration: DatabaseConfiguration): MC2FADatabase {
            return MySQLDatabase(databaseConfiguration)
        }
    }
}
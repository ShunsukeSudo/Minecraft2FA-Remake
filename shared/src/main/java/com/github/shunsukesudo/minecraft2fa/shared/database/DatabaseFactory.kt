package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseType

object DatabaseFactory {
    fun newConnection(databaseConfiguration: DatabaseConfiguration): MC2FADatabase {
        return when(databaseConfiguration.databaseType) {
            DatabaseType.SQLITE -> SQLiteDatabase(databaseConfiguration)
            DatabaseType.MYSQL -> MySQLDatabase(databaseConfiguration)
        }
    }
}
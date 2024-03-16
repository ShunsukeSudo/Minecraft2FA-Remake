package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.database.auth.AuthenticationSQLiteImpl
import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationSQLiteImpl
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal class SQLiteDatabase(
    databaseAddress: String,
    databaseName: String
) : MC2FADatabase {

    init {
        TransactionManager.defaultDatabase = Database.connect(databaseAddress+databaseName, driver = "org.sqlite.JDBC")
    }

    override fun getAuthentication(): Authentication {
        return AuthenticationSQLiteImpl()
    }

    override fun getIntegration(): Integration {
        return IntegrationSQLiteImpl()
    }
}
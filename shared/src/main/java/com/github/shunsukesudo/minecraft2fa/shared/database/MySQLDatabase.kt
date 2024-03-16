package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.database.auth.AuthenticationMySQLImpl
import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationMySQLImpl
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal class MySQLDatabase(
    databaseAddress: String,
    databaseName: String
) : MC2FADatabase {

    init {
        TransactionManager.defaultDatabase = Database.connect(databaseAddress+databaseName, driver = "com.mysql.cj.jdbc.Driver")
    }

    override fun getAuthentication(): Authentication {
        return AuthenticationMySQLImpl()
    }

    override fun getIntegration(): Integration {
        return IntegrationMySQLImpl()
    }

}
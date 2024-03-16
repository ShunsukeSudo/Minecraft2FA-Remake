package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal class SQLiteDatabase(
    databaseAddress: String,
    databaseName: String,
    user: String,
    password: String
) : MC2FADatabase {

    private val database = Database.connect(databaseAddress+databaseName, driver = "org.sqlite.JDBC", user = user, password = password)
    private val authentication = Authentication(this.database)
    private val integration = Integration(this.database)

    override fun authentication(): Authentication {
        return this.authentication
    }

    override fun integration(): Integration {
        return this.integration
    }

    override fun getDatabaseConnection(): Database {
        return this.database
    }
}
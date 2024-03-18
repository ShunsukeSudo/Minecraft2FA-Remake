package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal class SQLiteDatabase(
    databaseName: String,
) : MC2FADatabase {

    private val database = Database.connect(url = "jdbc:sqlite://$databaseName", driver = "org.sqlite.JDBC")
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
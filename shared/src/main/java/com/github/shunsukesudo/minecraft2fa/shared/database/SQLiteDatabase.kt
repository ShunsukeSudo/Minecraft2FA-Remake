package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.configuration.PluginConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

internal class SQLiteDatabase(
    databaseConfiguration: DatabaseConfiguration,
) : MC2FADatabase {

    private val database = Database.connect(
        url = "jdbc:sqlite://${databaseConfiguration.address}",
        driver = "org.sqlite.JDBC"
    )
    private val authentication = Authentication(this.database)
    private val integration = Integration(this.database)

    init {
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

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
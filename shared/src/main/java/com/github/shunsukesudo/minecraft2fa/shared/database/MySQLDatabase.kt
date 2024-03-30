package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.configuration.PluginConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager

internal class MySQLDatabase(
    databaseConfiguration: DatabaseConfiguration
) : MC2FADatabase {

    private val database = Database.connect(
        url = "jdbc:mySQL://${databaseConfiguration.address}",
        driver = "com.mysql.cj.jdbc.Driver",
        user = databaseConfiguration.userName,
        password = databaseConfiguration.userName
    )
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
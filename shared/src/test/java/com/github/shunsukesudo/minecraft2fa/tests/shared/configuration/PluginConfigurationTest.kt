package com.github.shunsukesudo.minecraft2fa.tests.shared.configuration

import com.github.shunsukesudo.minecraft2fa.shared.configuration.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PluginConfigurationTest {


    @Test
    fun `Test - should return expected value after instanced`() {
        val authConfigExpectedSessionExpireTimeInSeconds = 50
        val authConfiguration = AuthenticationConfiguration(authConfigExpectedSessionExpireTimeInSeconds)

        val dbConfigExpectedDatabaseType = DatabaseType.MYSQL
        val dbConfigExpectedAddress = "Address.adr:1000"
        val dbConfigExpectedUserName = "UserName"
        val dbConfigExpectedPassword = "PAssWoRd"
        val databaseConfiguration = DatabaseConfiguration(dbConfigExpectedDatabaseType, dbConfigExpectedAddress, dbConfigExpectedUserName, dbConfigExpectedPassword)

        val discordConfigExpectedToken = "Token"
        val discordBotConfiguration = DiscordBotConfiguration(discordConfigExpectedToken)


        val pluginConfiguration = PluginConfiguration(authConfiguration, databaseConfiguration, discordBotConfiguration)

        println("-- Auth config")
        println("Expected: $authConfigExpectedSessionExpireTimeInSeconds | Actual: ${pluginConfiguration.authConfiguration.sessionExpireTimeInSeconds}")
        Assertions.assertEquals(
            authConfigExpectedSessionExpireTimeInSeconds,
            pluginConfiguration.authConfiguration.sessionExpireTimeInSeconds
        )

        println("-- Database config")
        println("Expected: $dbConfigExpectedDatabaseType | Actual: ${pluginConfiguration.databaseConfiguration.databaseType}")
        Assertions.assertEquals(
            dbConfigExpectedDatabaseType,
            pluginConfiguration.databaseConfiguration.databaseType
        )
        println("Expected: $dbConfigExpectedAddress | Actual: ${pluginConfiguration.databaseConfiguration.address}")
        Assertions.assertEquals(
            dbConfigExpectedAddress,
            pluginConfiguration.databaseConfiguration.address
        )
        println("Expected: $dbConfigExpectedUserName | Actual: ${pluginConfiguration.databaseConfiguration.userName}")
        Assertions.assertEquals(
            dbConfigExpectedUserName,
            pluginConfiguration.databaseConfiguration.userName
        )
        println("Expected: $dbConfigExpectedPassword | Actual: ${pluginConfiguration.databaseConfiguration.password}")
        Assertions.assertEquals(
            dbConfigExpectedPassword,
            pluginConfiguration.databaseConfiguration.password
        )

        println("-- DiscordBot config")
        println("Expected: $discordConfigExpectedToken | Actual: ${pluginConfiguration.discordBotConfiguration.botToken}")
        Assertions.assertEquals(
            discordConfigExpectedToken,
            pluginConfiguration.discordBotConfiguration.botToken
        )


    }
}
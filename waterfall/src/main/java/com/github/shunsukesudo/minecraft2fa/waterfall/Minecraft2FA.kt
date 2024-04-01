package com.github.shunsukesudo.minecraft2fa.waterfall

import com.github.shunsukesudo.minecraft2fa.shared.configuration.*
import com.github.shunsukesudo.minecraft2fa.shared.database.DatabaseFactory
import com.github.shunsukesudo.minecraft2fa.shared.discord.DiscordBot
import net.dv8tion.jda.api.exceptions.InvalidTokenException
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import kotlin.math.log

class Minecraft2FA: Plugin() {

    companion object {
        private lateinit var plugin: Plugin

        fun getInstance(): Plugin {
            return plugin
        }
    }

    override fun onEnable() {
        plugin = this
        val pluginConfiguration: PluginConfiguration

        try {
            pluginConfiguration = parseConfig()
        } catch (e: Exception) {
            slF4JLogger.error("Plugin failed to start due to error!")
            e.printStackTrace()
            onDisable()
            return
        }

        val databaseConnection = DatabaseFactory.newConnection(pluginConfiguration.databaseConfiguration)
        try {
            DiscordBot(pluginConfiguration.discordBotConfiguration, databaseConnection)
        } catch (e: InvalidTokenException) {
            slF4JLogger.error("Provided token is invalid! Check your config!")
            e.printStackTrace()
            onDisable()
            return
        }
    }

    override fun onDisable() {

    }


    private fun parseConfig(): PluginConfiguration {
        makeConfig()
        val configuration = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "config.yml"))

        val sessionExpireTimeInSeconds = configuration.getInt("totp_auth.sessionExpireTimeInSeconds", -1)

        val databaseType = DatabaseType.valueOf(configuration.getString("database.type"))
        val databaseAddress = configuration.getString("database.address")
        val databaseUserName = configuration.getString("database.username")
        val databasePassword = configuration.getString("database.password")

        val discordBotToken = configuration.getString("discord.bot_token")

        val authenticationConfiguration = AuthenticationConfiguration(sessionExpireTimeInSeconds)

        val databaseConfiguration = DatabaseConfiguration(
            databaseType,
            databaseAddress,
            databaseUserName,
            databasePassword
        )

        val discordBotConfiguration = DiscordBotConfiguration(discordBotToken)

        return PluginConfiguration(
            authenticationConfiguration,
            databaseConfiguration,
            discordBotConfiguration
        )
    }

    private fun makeConfig() {
        if(!dataFolder.exists()) {
            logger.info("Config folder not exists! creating...")
            if(dataFolder.mkdir()) {
                logger.info("Config folder created!")
            }
            else {
                logger.info("Failed to create config folder!")
            }
        }

        val configFile = File(dataFolder, "config.yml")

        if(!configFile.exists()) {
            val outputStream = FileOutputStream(configFile)
            val inputStream = getResourceAsStream("config.yml")
            inputStream.transferTo(outputStream)
        }
    }
}
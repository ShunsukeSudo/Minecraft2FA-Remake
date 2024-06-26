package com.github.shunsukesudo.minecraft2fa.waterfall

import com.github.shunsukesudo.minecraft2fa.shared.configuration.*
import com.github.shunsukesudo.minecraft2fa.shared.database.DatabaseFactory
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import com.github.shunsukesudo.minecraft2fa.shared.discord.DiscordBot
import com.github.shunsukesudo.minecraft2fa.shared.event.MC2FAEvent
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.IPlugin
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.SharedPlugin
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayer
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayerWaterfall
import com.github.shunsukesudo.minecraft2fa.waterfall.commands.MC2FACommandWaterfall
import com.github.shunsukesudo.minecraft2fa.waterfall.events.AuthSessionExpireEventListenerWaterfall
import com.github.shunsukesudo.minecraft2fa.waterfall.events.AuthSuccessEventListenerWaterfall
import com.github.shunsukesudo.minecraft2fa.waterfall.events.CommandExecuteEventListener
import net.dv8tion.jda.api.exceptions.InvalidTokenException
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.TimeUnit

class Minecraft2FA: Plugin(), IPlugin {

    companion object{
        private lateinit var pluginConfig: PluginConfiguration
        private lateinit var pluginDatabase: MC2FADatabase
        private lateinit var pluginInstance: Plugin

        fun getPluginConfig(): PluginConfiguration {
            return pluginConfig
        }

        fun getDatabase(): MC2FADatabase {
            return pluginDatabase
        }

        fun getInstance(): Plugin {
            return pluginInstance
        }
    }

    init {
        SharedPlugin.plugin = this
        pluginInstance = this
    }

    override val pluginConfiguration: PluginConfiguration
        get() = getPluginConfig()
    override val database: MC2FADatabase
        get() = getDatabase()

    override fun runTask(runnable: Runnable, delay: Long, unit: TimeUnit) {
        proxy.scheduler.schedule(this, runnable, delay, unit)
    }

    override fun findPlayer(uuid: UUID): SharedPlayer {
        return SharedPlayerWaterfall(proxy.getPlayer(uuid))
    }

    override fun onEnable() {
        try {
            pluginConfig = parseConfig()
        } catch (e: Exception) {
            slF4JLogger.error("Plugin failed to start due to error!")
            e.printStackTrace()
            onDisable()
            return
        }

        val databaseConnection = DatabaseFactory.newConnection(pluginConfiguration.databaseConfiguration)
        pluginDatabase = databaseConnection

        try {
            DiscordBot(pluginConfiguration.discordBotConfiguration, databaseConnection)
        } catch (e: InvalidTokenException) {
            slF4JLogger.error("Provided token is invalid! Check your config!")
            e.printStackTrace()
            onDisable()
            return
        }

        proxy.pluginManager.registerCommand(this, MC2FACommandWaterfall())

        proxy.registerChannel("mc2fa:authentication")
        proxy.pluginManager.registerListener(this, CommandExecuteEventListener())

        MC2FAEvent.addListener(AuthSuccessEventListenerWaterfall())
        MC2FAEvent.addListener(AuthSessionExpireEventListenerWaterfall())
    }

    private fun parseConfig(): PluginConfiguration {
        makeConfig()
        val configuration = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "config.yml"))

        val sessionExpireTimeInSeconds = configuration.getInt("totp_auth.sessionExpireTimeInSeconds", -1)
        val totpIssuerName = configuration.getString("totp_auth.totpIssuerName", "MC2FA")

        val databaseType = DatabaseType.valueOf(configuration.getString("database.type"))
        var databaseAddress = configuration.getString("database.address")
        if(databaseType == DatabaseType.SQLITE) {
            databaseAddress = "${dataFolder.canonicalPath}/$databaseAddress"
        }

        val databaseUserName = configuration.getString("database.username")
        val databasePassword = configuration.getString("database.password")

        val discordBotToken = configuration.getString("discord.bot_token")

        val authenticationConfiguration = AuthenticationConfiguration(sessionExpireTimeInSeconds, totpIssuerName)

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
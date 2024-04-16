package com.github.shunsukesudo.minecraft2fa.paper

import com.github.shunsukesudo.minecraft2fa.shared.configuration.*
import com.github.shunsukesudo.minecraft2fa.shared.database.DatabaseFactory
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import com.github.shunsukesudo.minecraft2fa.shared.discord.DiscordBot
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.IPlugin
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.SharedPlugin
import net.dv8tion.jda.api.exceptions.InvalidTokenException
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Minecraft2FA: JavaPlugin(), IPlugin {

    companion object {
        private lateinit var pluginConfig: PluginConfiguration
        private lateinit var pluginDatabase: MC2FADatabase

        fun getPluginConfig(): PluginConfiguration {
            return pluginConfig
        }

        fun getDatabase(): MC2FADatabase {
            return pluginDatabase
        }
    }

    init {
        SharedPlugin.plugin = this
    }

    override val pluginConfiguration: PluginConfiguration
        get() = getPluginConfig()
    override val database: MC2FADatabase
        get() = getDatabase()

    override fun onEnable() {
        val isBungeeEnabled = Bukkit.spigot().config.getBoolean("settings.bungeecord")

        try {
            pluginConfig = parseConfig()
        } catch (e: Exception) {
            slF4JLogger.error("Plugin failed to start due to error!")
            e.printStackTrace()
            this.server.pluginManager.disablePlugin(this)
            return
        }




        if(!isBungeeEnabled) {
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
        }

    }

    override fun onDisable() {

    }


    private fun parseConfig(): PluginConfiguration {
        this.saveDefaultConfig()
        val bukkitConfiguration = this.config

        val sessionExpireTimeInSeconds = bukkitConfiguration.getInt("totp_auth.sessionExpireTimeInSeconds", -1)

        val databaseType = DatabaseType.valueOf(bukkitConfiguration.getString("database.type") ?: "")
        var databaseAddress = bukkitConfiguration.getString("database.address") ?: ""
        if(databaseType == DatabaseType.SQLITE) {
            databaseAddress = "${dataFolder.canonicalPath}/$databaseAddress"
        }

        val databaseUserName = bukkitConfiguration.getString("database.username") ?: ""
        val databasePassword = bukkitConfiguration.getString("database.password") ?: ""

        val discordBotToken = bukkitConfiguration.getString("discord.bot_token") ?: ""

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
}
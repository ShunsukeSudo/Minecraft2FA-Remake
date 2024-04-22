package com.github.shunsukesudo.minecraft2fa.paper

import com.github.shunsukesudo.minecraft2fa.paper.events.PlayerJoinListener
import com.github.shunsukesudo.minecraft2fa.paper.events.PluginMessagingChannelListener
import com.github.shunsukesudo.minecraft2fa.shared.configuration.*
import com.github.shunsukesudo.minecraft2fa.shared.database.DatabaseFactory
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import com.github.shunsukesudo.minecraft2fa.shared.discord.DiscordBot
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.IPlugin
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.SharedPlugin
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayer
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayerPaper
import net.dv8tion.jda.api.exceptions.InvalidTokenException
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.TimeUnit

class Minecraft2FA: JavaPlugin(), IPlugin {

    companion object {
        private lateinit var pluginConfig: PluginConfiguration
        private lateinit var pluginDatabase: MC2FADatabase
        private lateinit var pluginInstance: JavaPlugin

        fun getPluginConfig(): PluginConfiguration {
            return pluginConfig
        }

        fun getDatabase(): MC2FADatabase {
            return pluginDatabase
        }

        fun getInstance(): JavaPlugin {
            return pluginInstance
        }
    }

    init {
        SharedPlugin.plugin = this
        pluginInstance = this
    }

    private var isBungeeEnabled = false
    private lateinit var discordBot: DiscordBot

    override val pluginConfiguration: PluginConfiguration
        get() = getPluginConfig()
    override val database: MC2FADatabase
        get() = getDatabase()

    override fun runTask(runnable: Runnable, delay: Long, unit: TimeUnit) {
        val delayTime = when(unit) {
            TimeUnit.NANOSECONDS -> delay/1000/1000/1000*20
            TimeUnit.MICROSECONDS -> delay/1000/1000*20
            TimeUnit.MILLISECONDS -> delay/1000*20
            TimeUnit.SECONDS -> delay*20
            TimeUnit.MINUTES -> delay*60*20
            TimeUnit.HOURS -> delay*60*60*20
            TimeUnit.DAYS -> delay*60*60*24*20
        }
        server.scheduler.runTaskLater(this, runnable, delayTime)
    }

    override fun findPlayer(uuid: UUID): SharedPlayer? {
        val player = server.getPlayer(uuid)
        return if(player != null) SharedPlayerPaper(player) else null
    }

    override fun onEnable() {
        isBungeeEnabled = Bukkit.spigot().config.getBoolean("settings.bungeecord")

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
                discordBot = DiscordBot(pluginConfiguration.discordBotConfiguration, databaseConnection)
            } catch (e: InvalidTokenException) {
                slF4JLogger.error("Provided token is invalid! Check your config!")
                e.printStackTrace()
                onDisable()
                return
            }
        }
        else {
            server.pluginManager.registerEvents(PlayerJoinListener(), this)
            server.messenger.registerOutgoingPluginChannel(this, "mc2fa:authentication")
            server.messenger.registerIncomingPluginChannel(this, "mc2fa:authentication", PluginMessagingChannelListener())
        }
    }

    override fun onDisable() {
        if(!isBungeeEnabled) {
            discordBot.jda.shutdownNow()
        }
        else {
            server.messenger.unregisterOutgoingPluginChannel(this)
            server.messenger.unregisterIncomingPluginChannel(this)
        }
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
package com.github.shunsukesudo.minecraft2fa.shared.discord

import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import dev.creativition.simplejdautil.SimpleJDAUtil
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.requests.GatewayIntent

class DiscordBot(
    discordBotToken: String,
    database: MC2FADatabase
) {

    companion object {
        private lateinit var db: MC2FADatabase

        @JvmStatic
        fun getDatabaseConnection(): MC2FADatabase {
            return db
        }

        @JvmStatic
        fun replyErrorMessage(event: GenericCommandInteractionEvent, message: String) {
            event.reply("An error occurred!!: $message").setEphemeral(true).queue()
        }

        @JvmStatic
        fun replyErrorMessage(event: ModalInteractionEvent, message: String) {
            event.reply("An error occurred!!: $message").setEphemeral(true).queue()
        }
    }

    val jda: JDA

    init {
        val jdaBuilder = JDABuilder.createDefault(discordBotToken)

        jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT)

        SimpleJDAUtil.addSearchPath("com.github.shunsukesudo.minecraft2fa.shared.discord.commands.")

        jda = SimpleJDAUtil
            .registerListeners(jdaBuilder, ClassLoader.getPlatformClassLoader())
            .build()

        SimpleJDAUtil.registerSlashCommands(jda)

        db = database
    }
}
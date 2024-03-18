package com.github.shunsukesudo.minecraft2fa.shared.discord

import dev.creativition.simplejdautil.SimpleJDAUtil
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent

class DiscordBot(discordBotToken: String) {

    val jda: JDA

    init {
        val jdaBuilder = JDABuilder.createDefault(discordBotToken)

        jdaBuilder.enableIntents(GatewayIntent.MESSAGE_CONTENT)

        SimpleJDAUtil.addSearchPath("com.github.shunsukesudo.minecraft2fa.shared.discord.commands.")
        SimpleJDAUtil.addSearchPath("com.github.shunsukesudo.minecraft2fa.shared.discord.completions.")

        jda = SimpleJDAUtil
            .registerListeners(jdaBuilder, ClassLoader.getPlatformClassLoader())
            .build()

        SimpleJDAUtil.registerSlashCommands(jda)


    }
}
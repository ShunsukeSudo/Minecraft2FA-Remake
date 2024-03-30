package com.github.shunsukesudo.minecraft2fa.shared.configuration

data class DiscordBotConfiguration(
    val botToken: String
) {
    init {
        if(botToken.isEmpty())
            throw IllegalArgumentException("Bot token is should not empty!")
    }
}
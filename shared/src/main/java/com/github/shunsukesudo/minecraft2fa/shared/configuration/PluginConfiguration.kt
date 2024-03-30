package com.github.shunsukesudo.minecraft2fa.shared.configuration

data class PluginConfiguration(
    val authConfiguration: AuthenticationConfiguration,
    val databaseConfiguration: DatabaseConfiguration,
    val discordBotConfiguration: DiscordBotConfiguration
) {
}
package com.github.shunsukesudo.minecraft2fa.shared.database.integration

internal class IntegrationMySQLImpl(
) : Integration {

    override fun isIntegrationInformationExists(discordID: Int): Boolean {
        return false
    }

    override fun isIntegrationInformationExists(MinecraftUUID: String): Boolean {
        return false
    }

    override fun isIntegrationInformationExists(DiscordID: Int, MinecraftUUID: String): Boolean {
        return false
    }

    override fun addIntegrationInformation(discordID: Int, MinecraftUUID: String){
    }

    override fun removeIntegrationInformation(discordID: Int, MinecraftUUID: String){
    }

    override fun updateIntegrationInformation(discordID: Int, NewMinecraftUUID: String){
    }

    override fun updateIntegrationInformation(MinecraftUUID: String, NewDiscordID: Int){
    }

    override fun getPlayerID(MinecraftUUID: String): Int {
        return -1
    }

    override fun getPlayerID(discordID: Int): Int {
        return -1
    }

    override fun getMinecraftUUIDFromDiscordID(DiscordID: Int): String? {
        return null
    }

    override fun getDiscordIDFromMinecraftUUID(MinecraftUUID: String): Int {
        return -1
    }
}
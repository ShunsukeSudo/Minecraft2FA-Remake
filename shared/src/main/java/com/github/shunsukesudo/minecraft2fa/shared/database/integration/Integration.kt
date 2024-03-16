package com.github.shunsukesudo.minecraft2fa.shared.database.integration

import org.jetbrains.exposed.sql.Database

class Integration(
    private val database: Database
){

    /**
     *
     * @param discordID Discord User ID
     * @return true if information exists, otherwise false
     */
    fun isIntegrationInformationExists(discordID: Long): Boolean {
        return false
    }

    /**
     *
     * @param MinecraftUUID Minecraft User UUID
     * @return true if information exists, otherwise false
     */
    fun isIntegrationInformationExists(MinecraftUUID: String): Boolean {
        return false
    }

    /**
     *
     * Search by player ID and MinecraftUUID and checks 2FA authentication information exists.
     *
     * @param DiscordID Discord User ID
     * @param MinecraftUUID Minecraft User UUID
     * @return true if information exists, otherwise false
     */
    fun isIntegrationInformationExists(DiscordID: Long, MinecraftUUID: String): Boolean {
        return false
    }

    /**
     *
     * Adds integration information to database.
     *
     * @param discordID Discord User ID
     * @param MinecraftUUID Minecraft User UUID
     */
    fun addIntegrationInformation(DiscordID: Long, MinecraftUUID: String) {

    }

    /**
     *
     * Removes integration information from database
     *
     * @param discordID Discord User ID
     * @param MinecraftUUID Minecraft User UUID
     */
    fun removeIntegrationInformation(DiscordID: Long, MinecraftUUID: String) {

    }

    /**
     *
     * Search by Discord ID and Update Minecraft UUID to new UUID.
     *
     * @param discordID Discord User ID
     * @param NewMinecraftUUID New Minecraft User UUID to update
     */
    fun updateIntegrationInformation(DiscordID: Long, NewMinecraftUUID: String) {

    }

    /**
     *
     * Search by Minecraft UUID and Update Discord ID to new ID.
     *
     * @param MinecraftUUID Minecraft User UUID
     * @param NewDiscordID New Discord ID to update
     */
    fun updateIntegrationInformation(MinecraftUUID: String, NewDiscordID: Long) {

    }

    /**
     * Search by Minecraft UUID and retrieves unique ID from database.
     *
     * @param MinecraftUUID Minecraft User UUID
     * @return Unique User ID if found, otherwise -1.
     */
    fun getPlayerID(MinecraftUUID: String): Int {
        return -1
    }

    /**
     *
     * Search by Discord ID and retrieves unique ID from database.
     *
     * @param discordID Discord User ID
     * @return Unique User ID if found, otherwise -1.
     */
    fun getPlayerID(DiscordID: Long): Int {
        return -1
    }

    /**
     *
     * Search by Discord ID and retrieves integrated Minecraft UUID from database.
     *
     * @param DiscordID Discord User ID
     * @return Minecraft UUID if found, otherwise null.
     */
    fun getMinecraftUUIDFromDiscordID(DiscordID: Long): String? {
        return null
    }

    /**
     *
     * Search by Minecraft UUID and retrieves integrated Discord ID from database.
     *
     * @param MinecraftUUID Minecraft User UUID
     * @return Discord ID if found, otherwise -1.
     */
    fun getDiscordIDFromMinecraftUUID(MinecraftUUID: String): Int {
        return -1
    }
}
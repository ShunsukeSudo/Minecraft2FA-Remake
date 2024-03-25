package com.github.shunsukesudo.minecraft2fa.shared.database.integration

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class Integration(
    private val database: Database
){

    /**
     *
     * @param discordID Discord User ID
     * @return true if information exists, otherwise false
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun isIntegrationInformationExists(discordID: Long): Boolean {
        if(discordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        var notExists = true
        transaction(database) {
            notExists = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.discordID eq discordID
            }.empty()
        }
        return !notExists
    }

    /**
     *
     * @param minecraftUUID Minecraft User UUID
     * @return true if information exists, otherwise false
     */
    fun isIntegrationInformationExists(minecraftUUID: UUID): Boolean {
        var notExists = true
        transaction(database) {
            notExists = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.minecraftUUID eq minecraftUUID.toString()
            }.empty()
        }
        return !notExists
    }

    /**
     *
     * Search with player ID and minecraftUUID. then checks 2FA authentication information exists.
     *
     * @param discordID Discord User ID
     * @param minecraftUUID Minecraft User UUID
     * @return true if information exists, otherwise false
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun isIntegrationInformationExists(discordID: Long, minecraftUUID: UUID): Boolean {
        if(discordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        var discordIDNotExists = true
        var minecraftUUIDNotExists = true
        transaction(database) {
            discordIDNotExists = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.discordID eq discordID
            }.empty()

            minecraftUUIDNotExists = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.minecraftUUID eq minecraftUUID.toString()
            }.empty()
        }
        return !discordIDNotExists && !minecraftUUIDNotExists
    }

    /**
     *
     * Adds integration information to database.
     *
     * @param discordID Discord User ID
     * @param minecraftUUID Minecraft User UUID
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun addIntegrationInformation(discordID: Long, minecraftUUID: UUID) {
        if(discordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        transaction(database) {
            IntegrationInfoTable.insert {
                it[IntegrationInfoTable.discordID] = discordID
                it[IntegrationInfoTable.minecraftUUID] = minecraftUUID.toString()
            }
        }
    }

    /**
     *
     * Removes integration information from database
     *
     * @param minecraftUUID Minecraft User UUID
     * @return Returns deleted rows count
     */
    fun removeIntegrationInformation(minecraftUUID: UUID): Int {
        var deleted = 0
        transaction(database) {
            deleted = IntegrationInfoTable.deleteWhere {
                IntegrationInfoTable.minecraftUUID eq minecraftUUID.toString()
            }
        }
        return deleted
    }

    /**
     *
     * Removes integration information from database
     *
     * @param discordID Discord User ID
     * @return Returns deleted rows count
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun removeIntegrationInformation(discordID: Long): Int {
        if(discordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        var deleted = 0
        transaction(database) {
            deleted = IntegrationInfoTable.deleteWhere { IntegrationInfoTable.discordID eq discordID }
        }
        return deleted
    }

    /**
     *
     * Search by Discord ID and Update Minecraft UUID to new UUID.
     *
     * @param discordID Discord User ID
     * @param newMinecraftUUID New Minecraft User UUID to update
     * @return Returns updated rows count
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun updateIntegrationInformation(discordID: Long, newMinecraftUUID: UUID): Int {
        if(discordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")
        
        var updated = 0
        transaction(database) {
            updated = IntegrationInfoTable.update({
                IntegrationInfoTable.discordID eq discordID
            }) {
                it[IntegrationInfoTable.minecraftUUID] = newMinecraftUUID.toString()
            }
        }
        return updated
    }

    /**
     *
     * Search by Minecraft UUID and Update Discord ID to new ID.
     *
     * @param minecraftUUID Minecraft User UUID
     * @param newDiscordID New Discord ID to update
     * @return Returns updated rows count
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun updateIntegrationInformation(minecraftUUID: UUID, newDiscordID: Long): Int {
        if(newDiscordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        var updated = 0
        transaction(database) {
            updated = IntegrationInfoTable.update({
                IntegrationInfoTable.minecraftUUID eq minecraftUUID.toString()
            }) {
                it[IntegrationInfoTable.discordID] = newDiscordID
            }
        }
        return updated
    }

    /**
     * Search by Minecraft UUID and retrieves unique ID from database.
     *
     * @param minecraftUUID Minecraft User UUID
     * @return Unique User ID if found, otherwise -1.
     */
    fun getPlayerID(minecraftUUID: UUID): Int {
        var playerID: List<Int> = Collections.emptyList()
        transaction(database) {
            playerID = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.minecraftUUID eq minecraftUUID.toString()
            }.map {
                it[IntegrationInfoTable.id].value
            }
        }
        return if(playerID.first() > 0) playerID.first() else -1
    }

    /**
     *
     * Search by Discord ID and retrieves unique ID from database.
     *
     * @param discordID Discord User ID
     * @return Unique User ID if found, otherwise -1.
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun getPlayerID(discordID: Long): Int {
        if(discordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        var playerID: List<Int> = Collections.emptyList()
        transaction(database) {
            playerID = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.discordID eq discordID
            }.map {
                it[IntegrationInfoTable.id].value
            }
        }
        return if(playerID.first() > 0) playerID.first() else -1
    }

    /**
     *
     * Search by Discord ID and retrieves integrated Minecraft UUID from database.
     *
     * @param discordID Discord User ID
     * @return Minecraft UUID if found, otherwise null.
     * @throws IllegalArgumentException When negative long in discordID
     */
    fun getMinecraftUUIDFromDiscordID(discordID: Long): String? {
        if(discordID > 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        var minecraftUUID: List<String> = Collections.emptyList()
        transaction(database) {
            minecraftUUID = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.discordID eq discordID
            }.map {
                it[IntegrationInfoTable.minecraftUUID]
            }
        }
        return minecraftUUID.first().ifEmpty { null }
    }

    /**
     *
     * Search by Minecraft UUID and retrieves integrated Discord ID from database.
     *
     * @param minecraftUUID Minecraft User UUID
     * @return Discord ID if found, otherwise -1.
     */
    fun getDiscordIDFromMinecraftUUID(minecraftUUID: UUID): Long {
        var discordID: List<Long> = Collections.emptyList()
        transaction(database) {
            discordID = IntegrationInfoTable.selectAll().where {
                IntegrationInfoTable.minecraftUUID eq minecraftUUID.toString()
            }.map {
                it[IntegrationInfoTable.discordID]
            }
        }
        return if(discordID.first() > 0) discordID.first() else -1
    }
}
package com.github.shunsukesudo.minecraft2fa.tests.shared.database.integration

import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseType
import com.github.shunsukesudo.minecraft2fa.shared.database.DatabaseFactory
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfoTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.random.Random

class IntegrationTest {

    companion object {
        private val random = Random(UUID.randomUUID().toString().filter { it.isDigit() }.take(16).toLong())
        private val dbPath = "${File(".").canonicalPath}/test-database.db"
        private val dbConfig = DatabaseConfiguration(DatabaseType.SQLITE, dbPath, "", "")
        val database: MC2FADatabase = DatabaseFactory.SQLite.newConnection(dbConfig)

        init {
            transaction(database.getDatabaseConnection()) {
                SchemaUtils.createMissingTablesAndColumns(IntegrationInfoTable)
            }
        }

        @JvmStatic
        @AfterAll
        fun removeDBFile() {
            val dbFile = File(dbPath)
            if (dbFile.exists())
                dbFile.delete()
        }
    }

    @Test
    fun `Test - Should return false when both or one of information wrong when using isIntegrationInformationExists()`() {
        println("=========== Test - Should return false when both or one of information wrong when using isIntegrationInformationExists()")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(expectedDiscordID, expectedUUID)

        val randomUUID = UUID.randomUUID()
        println("Calling isIntegrationInformationExists() with random UUID: $randomUUID")
        val actualWithRandomUUID = database.integration().isIntegrationInformationExists(expectedDiscordID, randomUUID)

        var randomDiscordID = random.nextLong()
        if(randomDiscordID < 0) randomDiscordID = randomDiscordID.inv()
        println("Calling isIntegrationInformationExists() with random DiscordID: $randomDiscordID")
        val actualWithRandomDiscordID = database.integration().isIntegrationInformationExists(randomDiscordID, expectedUUID)

        println("Calling isIntegrationInformationExists() with expected values. $expectedDiscordID | $expectedUUID")
        val actualWithExpectedValues = database.integration().isIntegrationInformationExists(expectedDiscordID, expectedUUID)

        println("Check existence check with unexpected UUID is false")
        Assertions.assertEquals(false, actualWithRandomUUID)
        println("Check existence check with unexpected DiscordID is false")
        Assertions.assertEquals(false, actualWithRandomDiscordID)
        println("Check existence check with expected values is true")
        Assertions.assertEquals(true, actualWithExpectedValues)

        println("Passed.")
    }

    @Test
    fun `Test - Should return true when both or one of information correct when using isIntegrationInformationExists()`() {
        println("=========== Test - Should return true when both or one of information correct when using isIntegrationInformationExists()")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(expectedDiscordID, expectedUUID)

        println("Calling isIntegrationInformationExists() with expectedUUID: $expectedUUID")
        val actualWithUUID = database.integration().isIntegrationInformationExists(expectedUUID)

        println("Calling isIntegrationInformationExists() with expectedDiscordID: $expectedDiscordID")
        val actualWithDiscordID = database.integration().isIntegrationInformationExists(expectedDiscordID)

        println("Calling isIntegrationInformationExists() with expected values: $expectedUUID, $expectedDiscordID")
        val actualWithExpectedValues = database.integration().isIntegrationInformationExists(expectedDiscordID, expectedUUID)

        println("Check existence check with expected UUID is true")
        Assertions.assertEquals(true, actualWithUUID)
        println("Check existence check with expected DiscordID is true")
        Assertions.assertEquals(true, actualWithDiscordID)
        println("Check existence check with expected values is true")
        Assertions.assertEquals(true, actualWithExpectedValues)

        println("Passed.")
    }

    @Test
    fun `Test - Should return false after information deleted with discordID`() {
        println("=========== Test - Should return false after information deleted with discordID")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(expectedDiscordID, expectedUUID)

        println("Removing integration information")
        database.integration().removeIntegrationInformation(expectedDiscordID)

        val actualWithExpectedValues = database.integration().isIntegrationInformationExists(expectedUUID)

        println("Check existence check with expected values is false")
        Assertions.assertEquals(false, actualWithExpectedValues)

        println("Passed.")
    }

    @Test
    fun `Test - Should return false after information deleted with UUID`() {
        println("=========== Test - Should return false after information deleted with UUID")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(expectedDiscordID, expectedUUID)

        println("Removing integration information")
        database.integration().removeIntegrationInformation(expectedUUID)

        val actualWithExpectedValues = database.integration().isIntegrationInformationExists(expectedUUID)

        println("Check existence check with expected values is false")
        Assertions.assertEquals(false, actualWithExpectedValues)

        println("Passed.")
    }

    @Test
    fun `Test - Should UUID is updated after using updateIntegrationInformation()`() {
        println("=========== Test - Should UUID is updated after using updateIntegrationInformation()")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val uuidBeforeUpdate = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $uuidBeforeUpdate")
        database.integration().addIntegrationInformation(expectedDiscordID, uuidBeforeUpdate)

        val newUUID = UUID.randomUUID()
        println("Generated New UUID: $newUUID")

        println("Calling updateIntegrationInformation() with expected DiscordID: $expectedDiscordID | new UUID: $newUUID")
        database.integration().updateIntegrationInformation(expectedDiscordID, newUUID)

        println("Check existence check with new value is true")
        val actualWithExpectedValues = database.integration().isIntegrationInformationExists(expectedDiscordID, newUUID)

        Assertions.assertEquals(true, actualWithExpectedValues)

        println("Passed.")
    }

    @Test
    fun `Test - Should discord ID is updated after using updateIntegrationInformation()`() {
        println("=========== Test - Should UUID is updated after using updateIntegrationInformation()")
        var discordIDBeforeUpdate = random.nextLong()
        if(discordIDBeforeUpdate < 0) discordIDBeforeUpdate = discordIDBeforeUpdate.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $discordIDBeforeUpdate, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(discordIDBeforeUpdate, expectedUUID)

        var newDiscordID = random.nextLong()
        if(newDiscordID < 0) newDiscordID = newDiscordID.inv()
        println("Generated New UUID: $newDiscordID")

        println("Calling updateIntegrationInformation() with expected UUID: $expectedUUID | new discord ID: $newDiscordID")
        database.integration().updateIntegrationInformation(expectedUUID, newDiscordID)

        println("Check existence check with new value is true")
        val actualWithExpectedValues = database.integration().isIntegrationInformationExists(newDiscordID, expectedUUID)

        Assertions.assertEquals(true, actualWithExpectedValues)

        println("Passed.")
    }

    @Test
    fun `Test - Should return same ID when using getPlayerID with discord ID or minecraftUUID`() {
        println("=========== Test - Should return same ID when using getPlayerID with discord ID or minecraftUUID")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(expectedDiscordID, expectedUUID)

        val playerIDFromDiscordID = database.integration().getPlayerID(expectedDiscordID)
        println("value of playerIDFromDiscordID is $playerIDFromDiscordID")

        val playerIDFromMinecraftUUID = database.integration().getPlayerID(expectedUUID)
        println("value of playerIDFromMinecraftUUID is $playerIDFromMinecraftUUID")

        println("Check player ID is same as expected value.")
        Assertions.assertEquals(playerIDFromDiscordID, playerIDFromMinecraftUUID)

        println("Passed.")
    }

    @Test
    fun `Test - Should return expected UUID when using getMinecraftUUIDFromDiscordID`() {
        println("=========== Test - Should return expected UUID when using getMinecraftUUIDFromDiscordID")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(expectedDiscordID, expectedUUID)

        val actualUUID = database.integration().getMinecraftUUIDFromDiscordID(expectedDiscordID)

        println("Check UUID is same as expected value.")
        Assertions.assertEquals(expectedUUID.toString(), actualUUID.toString())

        println("Passed.")
    }

    @Test
    fun `Test - Should return expected discordID when using getDiscordIDFromMinecraftUUID`() {
        println("=========== Test - Should return expected discordID when using getDiscordIDFromMinecraftUUID")
        var expectedDiscordID = random.nextLong()
        if(expectedDiscordID < 0) expectedDiscordID = expectedDiscordID.inv()
        val expectedUUID = UUID.randomUUID()

        println("Adding integration information with | DiscordID: $expectedDiscordID, UUID: $expectedUUID")
        database.integration().addIntegrationInformation(expectedDiscordID, expectedUUID)

        val actualDiscordID = database.integration().getDiscordIDFromMinecraftUUID(expectedUUID)

        println("Check Discord ID is same as expected value.")
        Assertions.assertEquals(expectedDiscordID, actualDiscordID)

        println("Passed.")
    }
}
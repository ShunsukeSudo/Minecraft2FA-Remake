package com.github.shunsukesudo.minecraft2fa.tests.shared.database.auth

import com.github.shunsukesudo.minecraft2fa.shared.database.DatabaseFactory
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import com.github.shunsukesudo.minecraft2fa.shared.database.auth.AuthBackupCodeTable
import com.github.shunsukesudo.minecraft2fa.shared.database.auth.AuthInfoTable
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfoTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*
import kotlin.random.Random

class AuthenticationTest {

    companion object {
        private val randomID = Random(UUID.randomUUID().toString().filter { it.isDigit() }.take(16).toLong())
        private val dbPath = "${File(".").canonicalPath}/test-database.db"
        val database: MC2FADatabase = DatabaseFactory.SQLite.newConnection(dbPath)

        init {
            val dbFile = File(dbPath)
            if (dbFile.exists())
                dbFile.delete()

            transaction(database.getDatabaseConnection()) {
                SchemaUtils.createMissingTablesAndColumns(AuthInfoTable)
                SchemaUtils.createMissingTablesAndColumns(AuthBackupCodeTable)
                SchemaUtils.createMissingTablesAndColumns(IntegrationInfoTable)
            }
        }
    }


    @Test
    fun `Test - User authentication information existence check should return true when registered`() {
        println("=========== Test - User authentication information existence check should return true when registered")

        val fakeDiscordID = randomID.nextLong()
        println("fakeDiscordID: $fakeDiscordID")

        println("Add fake integration information.")
        database.integration().addIntegrationInformation(fakeDiscordID, UUID.randomUUID().toString())

        val playerID = database.integration().getPlayerID(fakeDiscordID)
        println("playerID on database: $playerID")


        println("Call is2FAAuthenticationInformationExists() with 2FA information isn't exists yet")
        val actualBefore = database.authentication().is2FAAuthenticationInformationExists(database.integration().getPlayerID(fakeDiscordID))
        println("Actual existence of before adding 2FA information is: $actualBefore")

        println("Adding fake 2FA information")
        val codes:MutableList<Int> = mutableListOf()
        var randInt: Int
        for(i in 1..5) {
            randInt = randomID.nextInt()
            if(randInt < 0) randInt = randInt.inv()
            codes.add(randInt)
        }
        println("Backup codes: $codes")
        database.authentication().add2FAAuthenticationInformation(playerID, "SecretKey", codes)

        println("Call is2FAAuthenticationInformationExists() with 2FA information after registered")
        val actualAfter = database.authentication().is2FAAuthenticationInformationExists(database.integration().getPlayerID(fakeDiscordID))
        println("Actual existence of after adding 2FA information is: $actualAfter")

        println("Check actual existence of before adding 2FA information is false")
        Assertions.assertEquals(false, actualBefore)
        println("Check actual existence of after adding 2FA information is true")
        Assertions.assertEquals(true, actualAfter)

        println("Passed.")
    }

    @Test
    fun `Test - User backup code should returned when 2FA information registered`() {
        println("=========== Test - User backup code should returned when 2FA information registered")

        val fakeDiscordID = randomID.nextLong()
        println("fakeDiscordID: $fakeDiscordID")

        println("Add fake integration information.")
        database.integration().addIntegrationInformation(fakeDiscordID, UUID.randomUUID().toString())

        val playerID = database.integration().getPlayerID(fakeDiscordID)
        println("playerID on database: $playerID")

        val codes:MutableList<Int> = mutableListOf()
        var randInt: Int
        for(i in 1..5) {
            randInt = randomID.nextInt()
            if(randInt < 0) randInt = randInt.inv()
            codes.add(randInt)
        }
        println("Backup codes: $codes")
        database.authentication().add2FAAuthenticationInformation(playerID, "SecretKey", codes)

        val authID = database.authentication().getAuthID(playerID)
        val backupCodes = database.authentication().get2FABackUpCodes(authID)

        println("Checking codes")
        backupCodes.forEachIndexed { index, i ->
            println("Actual: ${backupCodes[index]} | Expected: ${codes[index]}")
            Assertions.assertEquals(backupCodes[index], codes[index])
        }
        println("Passed.")
    }

    @Test
    fun `Test - Auth information should updated`() {
        println("=========== Test - Auth information should updated")

        val fakeDiscordID = randomID.nextLong()
        println("fakeDiscordID: $fakeDiscordID")

        println("Add fake integration information.")
        database.integration().addIntegrationInformation(fakeDiscordID, UUID.randomUUID().toString())

        val playerID = database.integration().getPlayerID(fakeDiscordID)
        println("playerID on database: $playerID")

        println("Adding fake 2FA information")
        val codes:MutableList<Int> = mutableListOf()
        var randInt: Int
        for(i in 1..5) {
            randInt = randomID.nextInt()
            if(randInt < 0) randInt = randInt.inv()
            codes.add(randInt)
        }
        println("Backup codes: $codes")
        database.authentication().add2FAAuthenticationInformation(playerID, "SecretKey", codes)

        val authID = database.authentication().getAuthID(playerID)
        println("Auth ID: $authID")
        val secretKey = database.authentication().get2FASecretKey(playerID)
        println("Secret key: $secretKey")
        val backupCodes = database.authentication().get2FABackUpCodes(authID)
        println("Backup codes from DB: $backupCodes")

        println("Checking codes")
        backupCodes.forEachIndexed { index, i ->
            println("Actual: ${backupCodes[index]} | Expected: ${codes[index]}")
            Assertions.assertEquals(backupCodes[index], codes[index])
        }
        
        println("Updating information")
        
        val newCodes:MutableList<Int> = mutableListOf()
        for(i in 1..5) {
            randInt = randomID.nextInt()
            if(randInt < 0) randInt = randInt.inv()
            newCodes.add(randInt)
        }
        println("New backup codes: $newCodes")
        
        val newSecretKey = "New SecretKey"
        println("New secret key: $newSecretKey")
        
        database.authentication().update2FAAuthenticationInformation(playerID, newSecretKey, newCodes)
        
        val actualNewSecret = database.authentication().get2FASecretKey(playerID)
        val actualNewBackupCodes = database.authentication().get2FABackUpCodes(authID)

        Assertions.assertNotNull(actualNewSecret)
        Assertions.assertEquals(newSecretKey, actualNewSecret)

        actualNewBackupCodes.forEachIndexed { index, i ->
            println("Actual: ${actualNewBackupCodes[index]} | Expected: ${newCodes[index]}")
            Assertions.assertEquals(actualNewBackupCodes[index], newCodes[index])
        }
        
        println("Passed.")
    }
}
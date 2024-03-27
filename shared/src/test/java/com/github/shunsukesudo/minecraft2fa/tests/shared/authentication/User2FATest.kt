package com.github.shunsukesudo.minecraft2fa.tests.shared.authentication

import com.github.shunsukesudo.minecraft2fa.shared.authentication.User2FA
import com.warrenstrange.googleauth.GoogleAuthenticator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.random.Random

class User2FATest {

    companion object {
        lateinit var gAuth: GoogleAuthenticator

        @JvmStatic
        @BeforeAll
        fun setUpGAuth() {
            gAuth = GoogleAuthenticator()
        }
    }

    private val random = Random(UUID.randomUUID().toString().filter { it.isDigit() }.take(16).toLong())
    
    @Test
    fun `Test generate credential and verify code`() {
        println("=========== Test generate credential and verify code")
        println("Generating new credentials")
        val credential = User2FA.createNewCredentials(0L)
        val backUpCodes  = credential.getBackupCodes()
        println("Backup codes: $backUpCodes")
        val secretKey = credential.getSecretKey()
        println("Secret key: $secretKey")

        println("Try authorizing using secret key: $secretKey")
        Assertions.assertEquals(true, User2FA.authorize(secretKey, gAuth.getTotpPassword(secretKey)))

        println("Passed.")
    }

    @Test
    fun `Test generate credential and get credential from getter then verify code`() {
        println("=========== Test generate credential and get credential from getter then verify code")
        println("Generating new credentials")
        var id = random.nextLong()
        if(id < 0L) id = id.inv()
        println("Generated ID: $id")
        User2FA.createNewCredentials(id)
        val credential = User2FA.getCredentials(id)
        val backUpCodes  = credential?.getBackupCodes()
        println("Backup codes: $backUpCodes")
        val secretKey = credential?.getSecretKey().toString()
        println("Secret key: $secretKey")

        println("Try authorizing using secret key: $secretKey")
        Assertions.assertEquals(true, User2FA.authorize(secretKey, gAuth.getTotpPassword(secretKey)))

        println("Passed.")
    }

    @Test
    fun `Test generate credential with invalid ID`() {
        println("=========== Test generate credential with invalid ID")
        println("Generating new credentials")
        var id = random.nextLong()
        if(id > 0L) id = id.inv()
        println("Generated ID: $id")

        println("Check throws IllegalArgumentException")
        assertThrows<IllegalArgumentException> {
            User2FA.createNewCredentials(id)
        }

        println("Passed.")
    }

    @Test
    fun `Test get credential with invalid ID`() {
        println("=========== Test generate credential with invalid ID")
        println("Generating new credentials")
        var id = random.nextLong()
        if(id < 0L) id = id.inv()
        println("Generated ID: $id")
        User2FA.createNewCredentials(id)

        println("Check throws IllegalArgumentException")
        assertThrows<IllegalArgumentException> {
            User2FA.getCredentials(id.inv())
        }

        println("Passed.")
    }
}
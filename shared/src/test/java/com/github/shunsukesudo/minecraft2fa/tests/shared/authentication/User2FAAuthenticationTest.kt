package com.github.shunsukesudo.minecraft2fa.tests.shared.authentication

import com.github.shunsukesudo.minecraft2fa.shared.authentication.User2FAAuthentication
import com.warrenstrange.googleauth.GoogleAuthenticator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

class User2FAAuthenticationTest {

    companion object {
        lateinit var gAuth: GoogleAuthenticator

        @JvmStatic
        @BeforeAll
        fun setUpGAuth() {
            gAuth = GoogleAuthenticator()
        }
    }

    @Test
    fun `Test generate credential and verify code`() {
        println("=========== Test generate credential and verify code")
        println("Generating new credentials")
        val credential = User2FAAuthentication.createNewCredentials(0L)
        val backUpCodes  = credential.getBackupCodes()
        println("Backup codes: $backUpCodes")
        val secretKey = credential.getSecretKey()
        println("Secret key: $secretKey")

        println("Try authorizing using secret key: $secretKey")
        Assertions.assertEquals(true, User2FAAuthentication.authorize(secretKey, gAuth.getTotpPassword(secretKey)))
    }

    @Test
    fun `Test generate credential and get credential from getter then verify code`() {
        println("=========== Test generate credential and get credential from getter then verify code")
        println("Generating new credentials")
        val id = Random(50239523098).nextLong().inv()
        println("Generated ID: $id")
        User2FAAuthentication.createNewCredentials(id)
        val credential = User2FAAuthentication.getCredentials(id)
        val backUpCodes  = credential?.getBackupCodes()
        println("Backup codes: $backUpCodes")
        val secretKey = credential?.getSecretKey().toString()
        println("Secret key: $secretKey")

        println("Try authorizing using secret key: $secretKey")
        Assertions.assertEquals(true, User2FAAuthentication.authorize(secretKey, gAuth.getTotpPassword(secretKey)))
    }

    @Test
    fun `Test generate credential with invalid ID`() {
        println("=========== Test generate credential with invalid ID")
        println("Generating new credentials")
        val id = Random(50239523098).nextLong()
        println("Generated ID: $id")

        println("Check throws IllegalArgumentException")
        assertThrows<IllegalArgumentException> {
            User2FAAuthentication.createNewCredentials(id)
        }
    }

    @Test
    fun `Test get credential with invalid ID`() {
        println("=========== Test generate credential with invalid ID")
        println("Generating new credentials")
        val id = Random(50239523098).nextLong().inv()
        println("Generated ID: $id")
        User2FAAuthentication.createNewCredentials(id)

        println("Check throws IllegalArgumentException")
        assertThrows<IllegalArgumentException> {
            User2FAAuthentication.getCredentials(id.inv())
        }
    }
}
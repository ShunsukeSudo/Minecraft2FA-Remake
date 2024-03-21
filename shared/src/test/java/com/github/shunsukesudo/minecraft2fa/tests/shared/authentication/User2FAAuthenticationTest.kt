package com.github.shunsukesudo.minecraft2fa.tests.shared.authentication

import com.github.shunsukesudo.minecraft2fa.shared.authentication.User2FAAuthentication
import com.warrenstrange.googleauth.GoogleAuthenticator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

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
}
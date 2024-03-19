package com.github.shunsukesudo.minecraft2fa.shared.authentication

import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.GoogleAuthenticatorKey

class UserAuthentication(
    private val discordID: Long,
) {
    companion object {
        private val auth = GoogleAuthenticator()

        @JvmStatic
        fun authorize(secretKey: String, TOTPCode: Int): Boolean {
            return auth.authorize(secretKey, TOTPCode)
        }
    }

    private val gAuth = GoogleAuthenticator()
    private var credentials: GoogleAuthenticatorKey? = null

    fun generateCredentials() {
        credentials = gAuth.createCredentials(discordID.toString())
    }

    fun getBackupCodes(): List<Int> {
        if(credentials != null) {
            return credentials!!.scratchCodes
        }
        return emptyList()
    }

    fun getSecretKey(): String? {
        if(credentials != null) {
            return credentials!!.key
        }
        return null
    }
}
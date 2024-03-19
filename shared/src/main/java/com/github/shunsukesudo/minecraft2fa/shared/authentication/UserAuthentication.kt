package com.github.shunsukesudo.minecraft2fa.shared.authentication

import com.warrenstrange.googleauth.GoogleAuthenticator
import com.warrenstrange.googleauth.GoogleAuthenticatorKey

class UserAuthentication private constructor(
    private val discordID: Long,
) {
    companion object {
        private val auth = GoogleAuthenticator()
        private val credentials = HashMap<Long, UserAuthentication?>()

        @JvmStatic
        fun authorize(secretKey: String, TOTPCode: Int): Boolean {
            return auth.authorize(secretKey, TOTPCode)
        }


        /**
         *
         * Creates a new credentials.
         *
         * @param discordID Discord User ID
         * @return UserAuthentication instance
         */
        @JvmStatic
        fun createNewCredentials(discordID: Long): UserAuthentication {
            val cred = UserAuthentication(discordID)
            credentials[discordID] = cred
            return cred
        }


        /**
         *
         * Retrieves user credentials.
         *
         * @param discordID Discord User ID
         * @return UserAuthentication instance if found, otherwise null
         */
        @JvmStatic
        fun getCredentials(discordID: Long): UserAuthentication? {
            return credentials[discordID]
        }
    }

    private val gAuth = GoogleAuthenticator()
    private var credentials = gAuth.createCredentials(discordID.toString())


    fun getBackupCodes(): List<Int> {
        return credentials.scratchCodes

    }

    fun getSecretKey(): String {
        return credentials.key
    }
}
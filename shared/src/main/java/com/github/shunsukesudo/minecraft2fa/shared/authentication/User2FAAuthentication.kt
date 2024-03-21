package com.github.shunsukesudo.minecraft2fa.shared.authentication

import com.warrenstrange.googleauth.GoogleAuthenticator

class User2FAAuthentication private constructor() {
    companion object {
        private val auth = GoogleAuthenticator()
        private val credentials = HashMap<Long, User2FAAuthentication?>()

        @JvmStatic
        fun authorize(secretKey: String, TOTPCode: Int): Boolean {
            return auth.authorize(secretKey, TOTPCode)
        }


        /**
         *
         * Creates a new credentials.
         *
         * @param discordID Discord User ID
         * @return User2FAAuthentication instance
         */
        @JvmStatic
        fun createNewCredentials(discordID: Long): User2FAAuthentication {
            val cred = User2FAAuthentication()
            credentials[discordID] = cred
            return cred
        }


        /**
         *
         * Retrieves user credentials.
         *
         * @param discordID Discord User ID
         * @return User2FAAuthentication instance if found, otherwise null
         */
        @JvmStatic
        fun getCredentials(discordID: Long): User2FAAuthentication? {
            return credentials[discordID]
        }
    }

    private val gAuth = GoogleAuthenticator()
    private var credentials = gAuth.createCredentials()


    fun getBackupCodes(): List<Int> {
        return credentials.scratchCodes

    }

    fun getSecretKey(): String {
        return credentials.key
    }
}
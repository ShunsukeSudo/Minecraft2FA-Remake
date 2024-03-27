package com.github.shunsukesudo.minecraft2fa.shared.authentication

import com.warrenstrange.googleauth.GoogleAuthenticator

class User2FA private constructor() {
    companion object {
        private val auth = GoogleAuthenticator()
        private val credentials = HashMap<Long, User2FA?>()

        @JvmStatic
        fun authorize(secretKey: String, TOTPCode: Int): Boolean {
            return auth.authorize(secretKey, TOTPCode)
        }


        /**
         *
         * Creates a new credentials.
         *
         * @param discordID Discord User ID
         * @return User2FA instance
         */
        @JvmStatic
        fun createNewCredentials(discordID: Long): User2FA {
            if(discordID < 0) {
                throw IllegalArgumentException("Discord user ID must be positive long!")
            }
            val cred = User2FA()
            credentials[discordID] = cred
            return cred
        }


        /**
         *
         * Retrieves user credentials.
         *
         * @param discordID Discord User ID
         * @return User2FA instance if found, otherwise null
         */
        @JvmStatic
        fun getCredentials(discordID: Long): User2FA? {
            if(discordID < 0) {
                throw IllegalArgumentException("Discord user ID must be positive long!")
            }
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
package com.github.shunsukesudo.minecraft2fa.shared.database.auth

import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfo
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Authentication{

    /**
     *
     * Search by player ID and checks 2FA authentication information exists.
     *
     * @param playerID Unique user ID stored in integration table
     * @return true if exists, otherwise false.
     */
    fun is2FAAuthenticationInformationExists(playerID: Int): Boolean {
        return false
    }

    /**
     *
     * Adds 2FA authenticate information with Player ID, Secret key, Backup codes to database.
     *
     * @param playerID Unique user ID stored in integration table
     * @param SecretKey TOTP Secret key
     * @param BackUpCodes TOTP BackUp Codes
     */
    fun add2FAAuthenticationInformation(playerID: Int, SecretKey: String, BackUpCodes: List<String>){
        transaction {
            val userInfo = IntegrationInfo.selectAll().where { IntegrationInfo.id eq playerID }.map { it[IntegrationInfo.id] }

            val authinfo = AuthInformation.new {
                this.userID = userInfo.first()
                this.secretKey = secretKey
            }

            val backupCodes = AuthBackCodes.new {
                this.authID = authinfo.userID
                this.backupCodes = backupCodes
            }
        }
    }

    /**
     *
     * Updates 2FA authenticate information with Player ID, Secret key, Backup codes in database.
     *
     * @param playerID Unique user ID stored in integration table
     * @param SecretKey TOTP Secret key
     * @param BackUpCodes TOTP BackUp Codes
     */
    fun update2FAAuthenticationInformation(playerID: Int, SecretKey: String, BackUpCodes: List<String>){
    }

    /**
     *
     * Removes 2FA authenticate information from database.
     *
     * @param playerID Unique user ID stored in integration table
     */
    fun remove2FAAuthenticationInformation(playerID: Int){
    }

    /**
     *
     * Retrieves user 2FA secret key from database.
     *
     * @param playerID Unique user ID stored in integration table
     * @return Secret key if found, otherwise null
     */
    fun get2FASecretKey(playerID: Int): String? {
        return null
    }

    /**
     *
     * Retrieves user 2FA backup codes from database.
     *
     * @param playerID Unique user ID stored in integration table
     * @return Backup codes if found, otherwise empty list
     */
    fun get2FABackUpCodes(playerID: Int): List<Int> {
        return Collections.emptyList()
    }
}
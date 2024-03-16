package com.github.shunsukesudo.minecraft2fa.shared.database.auth

import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfoTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class Authentication(
    private val database: Database
){

    /**
     *
     * Search by player ID and checks 2FA authentication information exists.
     *
     * @param playerID Unique user ID stored in integration table
     * @return true if exists, otherwise false.
     */
    fun is2FAAuthenticationInformationExists(playerID: Int): Boolean {
        var notExists = true
        transaction(database) {
            notExists = AuthInfoTable.selectAll().where { AuthInfoTable.playerID eq playerID }.empty()
        }
        return !notExists
    }

    /**
     *
     * Adds 2FA authenticate information with Player ID, Secret key, Backup codes to database.
     *
     * @param playerID Unique user ID stored in integration table
     * @param SecretKey TOTP Secret key
     * @param BackUpCodes TOTP BackUp Codes
     */
    fun add2FAAuthenticationInformation(playerID: Int, secretKey: String, backupCodes: List<Int>){
        transaction(database) {
            val userInfo = IntegrationInfoTable.selectAll().where { IntegrationInfoTable.id eq playerID }.map { it[IntegrationInfoTable.id] }

            val authinfo = AuthInformation.new {
                this.playerID = userInfo.first()
                this.secretKey = secretKey
            }

            backupCodes.forEach {
                AuthBackCodes.new {
                    this.authID = authinfo.playerID
                    this.backupCodes = it
                }
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
     * @return Returns updated rows count
     */
    fun update2FAAuthenticationInformation(playerID: Int, secretKey: String, backupCodes: List<Int>): Int{
        var updated = 0
        transaction(database) {
            updated = AuthInfoTable.update({AuthInfoTable.playerID eq playerID}) {
                it[AuthInfoTable.secretKey] = secretKey
            }

            val authID = AuthInfoTable.selectAll().where {
                AuthInfoTable.playerID eq playerID
            }.map {
                it[AuthInfoTable.id]
            }

            AuthBackupCodeTable.deleteWhere {
                AuthBackupCodeTable.authID eq authID.first().value
            }

            backupCodes.forEach {
                AuthBackCodes.new {
                    this.authID = authID.first()
                    this.backupCodes = it
                }
            }
        }
        return updated
    }

    /**
     *
     * Removes 2FA authenticate information from database.
     *
     * @param playerID Unique user ID stored in integration table
     * @return Returns deleted rows count
     */
    fun remove2FAAuthenticationInformation(playerID: Int): Int{
        var deleted = 0
        transaction(database) {
            deleted = AuthInfoTable.deleteWhere { AuthInfoTable.playerID eq playerID }
        }
        return deleted
    }

    /**
     *
     * Retrieves user 2FA secret key from database.
     *
     * @param playerID Unique user ID stored in integration table
     * @return Secret key if found, otherwise null
     */
    fun get2FASecretKey(playerID: Int): String? {
        var secretKey: List<String> = Collections.emptyList()
        transaction(database) {
            secretKey = AuthInfoTable.selectAll().where { AuthInfoTable.playerID eq playerID }.map { it[AuthInfoTable.secretKey] }
        }
        return if(secretKey.isEmpty()) secretKey.first() else null
    }

    /**
     *
     * Retrieves user 2FA backup codes from database.
     *
     * @param authID Unique authID stored in auth info table
     * @return Backup codes if found, otherwise empty list
     */
    fun get2FABackUpCodes(authID: Int): List<Int> {
        var bc: List<Int> = Collections.emptyList()
        transaction(database) {
            bc = AuthBackupCodeTable.selectAll().where { AuthBackupCodeTable.authID eq authID }.map { it[AuthBackupCodeTable.backupCode] }
        }
        if(bc.isEmpty())
            return Collections.emptyList()

        return bc
    }
}
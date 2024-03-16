package com.github.shunsukesudo.minecraft2fa.shared.database.auth

import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfo
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

internal class AuthenticationSQLiteImpl(
) : Authentication {

    override fun is2FAAuthenticationInformationExists(playerID: Int): Boolean {
        return false
    }

    override fun add2FAAuthenticationInformation(playerID: Int, SecretKey: String, BackUpCodes: List<String>){
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

    override fun update2FAAuthenticationInformation(playerID: Int, SecretKey: String, BackUpCodes: List<String>){
    }

    override fun remove2FAAuthenticationInformation(playerID: Int){
    }

    override fun get2FASecretKey(playerID: Int): String? {
        return null
    }

    override fun get2FABackUpCodes(playerID: Int): List<Int> {
        return Collections.emptyList()
    }
}
package com.github.shunsukesudo.minecraft2fa.shared.database.auth

import java.util.*

internal class AuthenticationMySQLImpl(
) : Authentication {


    override fun is2FAAuthenticationInformationExists(playerID: Int): Boolean {
        return false
    }

    override fun add2FAAuthenticationInformation(playerID: Int, SecretKey: String, BackUpCodes: List<String>){
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
package com.github.shunsukesudo.minecraft2fa.shared.database.auth

import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfo
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object AuthInfo : IntIdTable("auth") {
    val userID = reference("player_id", IntegrationInfo, fkName = "id")
    val secretKey = varchar("secret_key", 512)
}

class AuthInformation(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<AuthInformation>(AuthInfo)

    var userID by AuthInfo.userID
    var secretKey by AuthInfo.secretKey
}

object AuthBackupCode : IntIdTable("auth_backup_codes") {
    val authID = reference("auth_id", AuthInfo, fkName = "id")
    val backUpCodes = varchar("2fa_backup_code", 16)
}

class AuthBackCodes(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthBackCodes>(AuthBackupCode)

    var authID by AuthBackupCode.authID
    var backupCodes by AuthBackupCode.backUpCodes
}
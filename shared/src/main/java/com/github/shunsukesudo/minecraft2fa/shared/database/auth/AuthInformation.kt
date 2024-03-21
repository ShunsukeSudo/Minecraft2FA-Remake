package com.github.shunsukesudo.minecraft2fa.shared.database.auth

import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfoTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object AuthInfoTable : IntIdTable("auth") {
    val playerID = reference("player_id", IntegrationInfoTable, fkName = "id")
    val secretKey = varchar("secret_key", 512)
}

class AuthInformation(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<AuthInformation>(AuthInfoTable)

    var playerID by AuthInfoTable.playerID
    var secretKey by AuthInfoTable.secretKey
}

object AuthBackupCodeTable : IntIdTable("auth_backup_codes") {
    val authID = reference("auth_id", AuthInfoTable, fkName = "id")
    val backUpCodes = integer("2fa_backup_code")
}

class AuthBackCodes(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthBackCodes>(AuthBackupCodeTable)

    var authID by AuthBackupCodeTable.authID
    var backupCodes by AuthBackupCodeTable.backUpCodes
}
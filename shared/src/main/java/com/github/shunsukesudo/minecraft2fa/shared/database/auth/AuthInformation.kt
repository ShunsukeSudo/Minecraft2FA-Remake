package com.github.shunsukesudo.minecraft2fa.shared.database.auth

import com.github.shunsukesudo.minecraft2fa.shared.database.integration.IntegrationInfoTable
import org.jetbrains.exposed.dao.id.IntIdTable


object AuthInfoTable : IntIdTable("auth") {
    val playerID = reference("player_id", IntegrationInfoTable, fkName = "id")
    val secretKey = varchar("secret_key", 512)
}

object AuthBackupCodeTable : IntIdTable("auth_backup_codes") {
    val authID = reference("auth_id", AuthInfoTable, fkName = "id")
    val backUpCodes = integer("2fa_backup_code")
}

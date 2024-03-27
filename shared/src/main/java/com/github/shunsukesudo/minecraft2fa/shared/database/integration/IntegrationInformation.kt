package com.github.shunsukesudo.minecraft2fa.shared.database.integration

import org.jetbrains.exposed.dao.id.IntIdTable

object IntegrationInfoTable : IntIdTable("players") {
    val minecraftUUID = varchar("minecraft_uuid", 255)
    val discordID = long("discord_id")
}
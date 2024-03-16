package com.github.shunsukesudo.minecraft2fa.shared.database.integration

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object IntegrationInfo : IntIdTable("players") {
    val minecraftUUID = varchar("minecraft_uuid", 255)
    val discordID = long("discord_id")
}

class IntegrationInformation(id: EntityID<Int>) : IntEntity(id){
    companion object : IntEntityClass<IntegrationInformation>(IntegrationInfo)

    var minecraftUUID by IntegrationInfo.minecraftUUID
    var discordID by IntegrationInfo.discordID
}
package com.github.shunsukesudo.minecraft2fa.shared.event.auth

import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent
import java.util.UUID

class AuthSuccessEvent(
    private val discordID: Long,
    private val minecraftUUID: UUID
): GenericEvent {

    fun getDiscordID(): Long {
        return this.discordID
    }

    fun getMinecraftUUID(): UUID {
        return this.minecraftUUID
    }
}
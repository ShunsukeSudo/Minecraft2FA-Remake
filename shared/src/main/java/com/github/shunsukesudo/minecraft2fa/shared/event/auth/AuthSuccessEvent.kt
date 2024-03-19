package com.github.shunsukesudo.minecraft2fa.shared.event.auth

import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent

class AuthSuccessEvent(
    private val discordID: Long
): GenericEvent {

    fun getDiscordID(): Long {
        return this.discordID
    }
}
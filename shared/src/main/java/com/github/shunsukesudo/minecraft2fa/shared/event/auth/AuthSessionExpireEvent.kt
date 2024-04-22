package com.github.shunsukesudo.minecraft2fa.shared.event.auth

import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent
import java.util.UUID

class AuthSessionExpireEvent (
    private val uuid: UUID
): GenericEvent {

    fun getUUID(): UUID {
        return this.uuid
    }
}
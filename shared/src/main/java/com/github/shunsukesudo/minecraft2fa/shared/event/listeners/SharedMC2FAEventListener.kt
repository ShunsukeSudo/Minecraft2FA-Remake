package com.github.shunsukesudo.minecraft2fa.shared.event.listeners

import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent

interface SharedMC2FAEventListener {
    fun onEvent(event: GenericEvent)
}
package com.github.shunsukesudo.minecraft2fa.shared.event.listeners

import com.github.shunsukesudo.minecraft2fa.shared.event.EventHandler
import com.github.shunsukesudo.minecraft2fa.shared.event.EventListener
import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent

abstract class MC2FAEventListener: EventListener {
    abstract val sharedEvent: SharedMC2FAEventListener
    @EventHandler
    fun onEvent(event: GenericEvent) {
        sharedEvent.onEvent(event)
    }
}
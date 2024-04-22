package com.github.shunsukesudo.minecraft2fa.paper.events

import com.github.shunsukesudo.minecraft2fa.shared.event.listeners.AuthSessionExpireEventListener
import com.github.shunsukesudo.minecraft2fa.shared.event.listeners.MC2FAEventListener
import com.github.shunsukesudo.minecraft2fa.shared.event.listeners.SharedMC2FAEventListener

class AuthSessionExpireEventListenerPaper(override val sharedEvent: SharedMC2FAEventListener = AuthSessionExpireEventListener()): MC2FAEventListener()
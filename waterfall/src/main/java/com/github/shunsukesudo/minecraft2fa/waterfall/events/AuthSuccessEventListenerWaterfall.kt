package com.github.shunsukesudo.minecraft2fa.waterfall.events

import com.github.shunsukesudo.minecraft2fa.shared.event.listeners.AuthSuccessEventListener
import com.github.shunsukesudo.minecraft2fa.shared.event.listeners.SharedMC2FAEventListener
import com.github.shunsukesudo.minecraft2fa.shared.event.listeners.MC2FAEventListener

class AuthSuccessEventListenerWaterfall(override val sharedEvent: SharedMC2FAEventListener = AuthSuccessEventListener()): MC2FAEventListener()
package com.github.shunsukesudo.minecraft2fa.shared.minecraft.player

import net.kyori.adventure.text.Component
import java.util.UUID

abstract class SharedPlayer {
    abstract val displayName: String
    abstract val uuid: UUID

    abstract fun sendMessage(message: Component)
}
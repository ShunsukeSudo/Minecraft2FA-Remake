package com.github.shunsukesudo.minecraft2fa.shared.minecraft.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.chat.ComponentSerializer
import java.util.*

class SharedPlayerWaterfall(private val proxiedPlayer: ProxiedPlayer): SharedPlayer() {
    override val displayName: String
        get() = proxiedPlayer.displayName
    override val uuid: UUID
        get() = proxiedPlayer.uniqueId

    override fun sendMessage(message: Component) {
        proxiedPlayer.sendMessage(*BungeeComponentSerializer.get().serialize(message))
    }
}
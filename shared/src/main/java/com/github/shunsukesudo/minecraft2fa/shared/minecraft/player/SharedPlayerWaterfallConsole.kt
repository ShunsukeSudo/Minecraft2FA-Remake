package com.github.shunsukesudo.minecraft2fa.shared.minecraft.player

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.md_5.bungee.api.CommandSender
import java.util.*

class SharedPlayerWaterfallConsole(private val commandSender: CommandSender): SharedPlayer() {
    override val displayName: String
        get() = "Console"
    override val uuid: UUID
        get() = UUID.fromString("0000-0000-0000-0000")

    override fun sendMessage(message: Component) {
        commandSender.sendMessage(*BungeeComponentSerializer.get().serialize(message))
    }
}
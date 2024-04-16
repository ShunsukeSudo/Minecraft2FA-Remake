package com.github.shunsukesudo.minecraft2fa.shared.minecraft.player

import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import java.util.*

class SharedPlayerPaperConsole(private val console: CommandSender): SharedPlayer() {
    override val displayName: String
        get() = console.name
    override val uuid: UUID
        get() = UUID.fromString("0000-0000-0000-0000")

    override fun sendMessage(message: Component) {
        console.sendMessage(message)
    }
}
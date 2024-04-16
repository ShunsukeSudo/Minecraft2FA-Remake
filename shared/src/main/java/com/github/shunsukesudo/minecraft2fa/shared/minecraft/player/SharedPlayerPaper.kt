package com.github.shunsukesudo.minecraft2fa.shared.minecraft.player

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import java.util.*

class SharedPlayerPaper(private val player: Player): SharedPlayer() {
    override val displayName: String
        get() = player.name
    override val uuid: UUID
        get() = player.uniqueId

    override fun sendMessage(message: Component) {
        player.sendMessage(message)
    }
}
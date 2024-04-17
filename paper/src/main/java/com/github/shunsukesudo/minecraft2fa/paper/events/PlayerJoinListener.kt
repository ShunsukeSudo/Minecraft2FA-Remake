package com.github.shunsukesudo.minecraft2fa.paper.events

import com.github.shunsukesudo.minecraft2fa.paper.Minecraft2FA
import com.google.common.io.ByteStreams
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener: Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val plugin = Minecraft2FA.getInstance()
        val player = event.player

        if(!player.hasPermission("mc2fa.connect"))
            return

        val outStream = ByteStreams.newDataOutput()
        outStream.writeUTF("AuthInformationRequest")
        outStream.writeUTF(event.player.uniqueId.toString())

        plugin.server.scheduler.runTaskLater(plugin, Runnable {
            event.player.sendPluginMessage(plugin, "mc2fa:authentication", outStream.toByteArray())
        }, 30)
    }
}
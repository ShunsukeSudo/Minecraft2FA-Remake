package com.github.shunsukesudo.minecraft2fa.paper.events

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuthStatus
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.lang.IllegalArgumentException
import java.util.*

class PluginMessagingChannelListener: PluginMessageListener {
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if(!channel.equals("mc2fa:authentication", ignoreCase = true))
            return

        val inps = ByteStreams.newDataInput(message)
        val subChannel = inps.readUTF()

        if(!subChannel.equals("authInformationShare", ignoreCase = true))
            return

        val uuid = UUID.fromString(inps.readUTF())
        val statusStr = inps.readUTF()

        val status = try {
            MCUserAuthStatus.valueOf(statusStr)
        } catch (e: IllegalArgumentException) {
            MCUserAuthStatus.NOT_AUTHORIZED
        }

        when(status) {
            MCUserAuthStatus.AUTHORIZED -> {
                MCUserAuth.setUserAuthorizationStatus(uuid, status)
            }
            else -> {
                MCUserAuth.setUserAuthorizationStatus(uuid, status)
            }
        }
    }
}
package com.github.shunsukesudo.minecraft2fa.waterfall.events

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuthStatus
import com.github.shunsukesudo.minecraft2fa.waterfall.Minecraft2FA
import com.google.common.io.ByteStreams
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.connection.Server
import net.md_5.bungee.api.event.PluginMessageEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.*

class PluginMessagingChannelListener: Listener {

    @EventHandler
    fun onPluginMessage(event: PluginMessageEvent) {
        if (!event.tag.equals("mc2fa:authentication", ignoreCase = true))
            return

        if(event.sender !is Server)
            return

        val inp = ByteStreams.newDataInput(event.data)
        val subChannel = inp.readUTF()
        val userUUID = inp.readUTF()

        if (!subChannel.equals("AuthInformationRequest", ignoreCase = true))
            return


        if(event.receiver !is ProxiedPlayer)
            return

        val receiver = event.receiver as ProxiedPlayer
        sendAuthData(receiver.server, UUID.fromString(userUUID))
    }

    private fun sendAuthData(server: Server, uuid: UUID){
        val networkPlayers = Minecraft2FA.getInstance().proxy.players
        if (networkPlayers.isNullOrEmpty()) return

        val outStream = ByteStreams.newDataOutput()
        outStream.writeUTF("authInformationShare")
        outStream.writeUTF(uuid.toString())
        outStream.writeUTF(MCUserAuth.getUserAuthorizationStatus(uuid).toString())

        server.sendData("mc2fa:authentication", outStream.toByteArray())
    }
}
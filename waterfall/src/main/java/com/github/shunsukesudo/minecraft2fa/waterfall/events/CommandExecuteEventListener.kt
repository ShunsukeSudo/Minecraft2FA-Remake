package com.github.shunsukesudo.minecraft2fa.waterfall.events

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuthStatus
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.MinecraftConstants
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.message.CommonMessages
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class CommandExecuteEventListener: Listener {

    @EventHandler
    fun onCommandExecute(event: ChatEvent) {
        if(event.sender !is ProxiedPlayer)
            return

        if(!event.isCommand)
            return

        val player = event.sender as ProxiedPlayer
        if(!player.hasPermission(MinecraftConstants.permission2faCommand))
            return

        val args = event.message.split(" ")
        if(args[0].equals(MinecraftConstants.commandName2FA, ignoreCase = true))
            return

        if(MCUserAuth.getUserAuthorizationStatus(player.uniqueId) == MCUserAuthStatus.AUTHORIZED)
            return

        player.sendMessage(CommonMessages.yourNotInVerifiedSession().getMessageWithPrefix().content())
        event.isCancelled = true
    }
}
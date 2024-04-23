package com.github.shunsukesudo.minecraft2fa.paper.events

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuthStatus
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.MinecraftConstants
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.message.CommonMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandExecuteEventListener: Listener {

    @EventHandler
    fun onCommandExecute(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        if(!player.hasPermission(MinecraftConstants.permission2faCommand))
            return

        val args = event.message.split(" ")
        if(args[0].equals("/${MinecraftConstants.commandName2FA}", ignoreCase = true))
            return

        if(MCUserAuth.getUserAuthorizationStatus(player.uniqueId) == MCUserAuthStatus.AUTHORIZED)
            return

        player.sendMessage(CommonMessages.yourNotInVerifiedSession().getMessageWithPrefix())
        event.isCancelled = true
    }
}
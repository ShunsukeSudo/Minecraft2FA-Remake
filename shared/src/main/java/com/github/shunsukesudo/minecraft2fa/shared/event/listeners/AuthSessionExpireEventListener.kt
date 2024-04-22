package com.github.shunsukesudo.minecraft2fa.shared.event.listeners

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuthStatus
import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent
import com.github.shunsukesudo.minecraft2fa.shared.event.auth.AuthSessionExpireEvent
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.SharedPlugin
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.message.CommonMessages
import net.kyori.adventure.text.Component

class AuthSessionExpireEventListener: SharedMC2FAEventListener {
    override fun onEvent(event: GenericEvent) {
        if(event !is AuthSessionExpireEvent)
            return

        val uuid = event.getUUID()

        val player = SharedPlugin.plugin.findPlayer(uuid)

        player?.sendMessage(CommonMessages.yourSessionExpired().getMessageWithPrefix())

        MCUserAuth.setUserAuthorizationStatus(uuid, MCUserAuthStatus.NOT_AUTHORIZED)
    }
}
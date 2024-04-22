package com.github.shunsukesudo.minecraft2fa.shared.event.listeners

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuthStatus
import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent
import com.github.shunsukesudo.minecraft2fa.shared.event.MC2FAEvent
import com.github.shunsukesudo.minecraft2fa.shared.event.auth.AuthSessionExpireEvent
import com.github.shunsukesudo.minecraft2fa.shared.event.auth.AuthSuccessEvent
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.SharedPlugin
import net.kyori.adventure.text.Component
import java.util.*
import java.util.concurrent.TimeUnit

class AuthSuccessEventListener : SharedMC2FAEventListener {
    override fun onEvent(event: GenericEvent) {
        if(event !is AuthSuccessEvent)
            return

        val discordID = event.getDiscordID()
        val uuidStr = SharedPlugin.database.integration().getMinecraftUUIDFromDiscordID(discordID).toString()
        val uuid = UUID.fromString(uuidStr)

        MCUserAuth.setUserAuthorizationStatus(uuid, MCUserAuthStatus.AUTHORIZED)

        val player = SharedPlugin.plugin.findPlayer(uuid)

        player?.sendMessage(Component.text("TODO_SESSION_VERIFIED"))

        val authExpireEvent = AuthSessionExpireEvent(uuid)
        SharedPlugin.plugin.runTask({
            MC2FAEvent.callEvent(authExpireEvent)
        }, SharedPlugin.pluginConfiguration.authConfiguration.sessionExpireTimeInSeconds.toLong(), TimeUnit.SECONDS)
    }
}
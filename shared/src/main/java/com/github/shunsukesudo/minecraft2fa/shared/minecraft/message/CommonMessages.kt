package com.github.shunsukesudo.minecraft2fa.shared.minecraft.message

object CommonMessages {

    fun yourSessionExpireIn(seconds: Int): PluginMessage {
        return PluginMessage("Your session expires in $seconds")
    }

    fun yourSessionExpired(): PluginMessage {
        return PluginMessage("Your session expired! You will not able to use any command from now. Verify again in discord to make command usable again.")
    }

    fun yourSessionVerified(): PluginMessage {
        return PluginMessage("Your session verified! You can use any command if you have right permission.")
    }
}
package com.github.shunsukesudo.minecraft2fa.shared.minecraft.message

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

class PluginMessage(override val message: String) : Message {
    override val prefix = "[MC2FA]"

    override fun getPrefix(): TextComponent {
        return Component.text(prefix)
    }

    override fun getMessage(): TextComponent {
        return Component.text(message)
    }

    override fun getMessageWithPrefix(): TextComponent {
        return Component.text("$prefix $message")
    }
}
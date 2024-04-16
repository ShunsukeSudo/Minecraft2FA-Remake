package com.github.shunsukesudo.minecraft2fa.shared.minecraft.message

import net.kyori.adventure.text.TextComponent

interface Message{
    val prefix: String
    val message: String

    /**
     *
     * Retrieves prefix defined in this message
     *
     * @return TextComponent
     */
    fun getPrefix(): TextComponent

    /**
     *
     * Retrieves message defined in this message
     *
     * @return TextComponent
     */
    fun getMessage(): TextComponent

    /**
     *
     * Retrieves all context in this message
     *
     * @return TextComponent
     */
    fun getMessageWithPrefix(): TextComponent
}
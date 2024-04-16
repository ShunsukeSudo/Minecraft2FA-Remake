package com.github.shunsukesudo.minecraft2fa.shared.minecraft.message

import net.kyori.adventure.text.Component

object ErrorMessages {

    /**
     *
     * @param args how many args required to command.
     * @return Message object
     */
    fun notEnoughArguments(args: Int = 0): PluginMessage {
        return when(args) {
            0 -> PluginMessage("Not enough arguments!")
            else -> PluginMessage("Not enough arguments! requires $args arguments.")
        }
    }

    fun invalidTokenSpecified(): PluginMessage {
        return PluginMessage("Invalid token specified!")
    }

    fun thisArgumentsDoesNotExists(argument: String): PluginMessage {
        return PluginMessage("This argument $argument does not exists!")
    }
}
package com.github.shunsukesudo.minecraft2fa.shared.minecraft.command

import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayerWaterfall
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayerWaterfallConsole
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor

abstract class WaterfallCommand(open val command: SharedCommand): Command(command.commandName, command.permission, *command.commandAliases), TabExecutor {
    abstract val sharedCommand: SharedCommand
    override fun execute(sender: CommandSender, args: Array<String>) {
        val cmdSender = when(sender) {
            is ProxiedPlayer -> SharedPlayerWaterfall(sender)
            else -> SharedPlayerWaterfallConsole(sender)
        }

        sharedCommand.execute(cmdSender, args)
    }

    override fun onTabComplete(sender: CommandSender, args: Array<String>): Iterable<String> {
        val cmdSender = when(sender) {
            is ProxiedPlayer -> SharedPlayerWaterfall(sender)
            else -> SharedPlayerWaterfallConsole(sender)
        }
        return sharedCommand.suggest(cmdSender, args)
    }

}
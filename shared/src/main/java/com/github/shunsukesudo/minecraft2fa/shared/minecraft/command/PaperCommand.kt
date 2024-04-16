package com.github.shunsukesudo.minecraft2fa.shared.minecraft.command

import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayerPaper
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayerPaperConsole
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

abstract class PaperCommand(): CommandExecutor, TabCompleter {
    abstract val sharedCommand: SharedCommand
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val cmdSender = when(sender) {
            is Player -> SharedPlayerPaper(sender)
            else -> SharedPlayerPaperConsole(sender)
        }

        return sharedCommand.execute(cmdSender, args)
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        val cmdSender = when(sender) {
            is Player -> SharedPlayerPaper(sender)
            else -> SharedPlayerPaperConsole(sender)
        }

        return sharedCommand.suggest(cmdSender, args)
    }
}
package com.github.shunsukesudo.minecraft2fa.waterfall.commands

import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import com.github.shunsukesudo.minecraft2fa.shared.integration.IntegrationManager
import com.github.shunsukesudo.minecraft2fa.waterfall.Minecraft2FA
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

class MC2FACommand: Command("mc2fa", "mc2fa.command") {
    override fun execute(sender: CommandSender?, args: Array<out String>?) {
        if(sender == null)
            return

        if(sender !is ProxiedPlayer)
            return

        if(args.isNullOrEmpty()) {
            val textComponent = TextComponent("Arguments required. Possible arguments: connect")
            sender.sendMessage(textComponent)
            return
        }

        if(args[0].lowercase() == "connect") {
            subCommandConnect(sender, args)
        }
    }


    private fun subCommandConnect(sender: CommandSender, args: Array<out String>) {
        if(args.size < 2) {
            val textComponent = TextComponent("Missing connection token! usage: /mc2fa connect <token_here>")
            sender.sendMessage(textComponent)
            return
        }

        val token = args[1]

        if(!IntegrationManager.isValidIntegrationToken(token)) {
            val textComponent = TextComponent("Invalid token specified!")
            sender.sendMessage(textComponent)
            return
        }

        try {
            val database = Minecraft2FA.getDatabase()
            val discordID = IntegrationManager.getDiscordIDFromToken(token)
            val player = sender as ProxiedPlayer
            database.integration().addIntegrationInformation(discordID, player.uniqueId)
        } catch (e: Exception) {
            e.printStackTrace()
            val textComponent = TextComponent("Something went wrong when integrating your account!")
            sender.sendMessage(textComponent)
            return
        }

        val textComponent = TextComponent("Successfully integrated your account with discord!")
    }
}
package com.github.shunsukesudo.minecraft2fa.shared.minecraft.command

import com.github.shunsukesudo.minecraft2fa.shared.integration.IntegrationManager
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.SharedPlugin
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.message.ErrorMessages
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayer
import net.kyori.adventure.text.Component
import java.lang.Exception

class MC2FACommand: SharedCommand {
    override val commandName = "mc2fa"
    override val commandAliases = arrayOf("")
    override val permission = "mc2fa.command"

    override fun execute(sender: SharedPlayer, args: Array<String>) {
        if(args.isEmpty())
            return

        if(args[0].lowercase() == "connect") {
            subCommandConnect(sender, args)
        }

        sender.sendMessage(ErrorMessages.thisArgumentsDoesNotExists(args[0]).getMessageWithPrefix())
    }

    override fun suggest(sender: SharedPlayer, args: Array<String>): List<String> {
        return emptyList()
    }

    private fun subCommandConnect(sender: SharedPlayer, args: Array<String>) {
        if(args.size < 2) {
            sender.sendMessage(ErrorMessages.notEnoughArguments(2).getMessageWithPrefix())
            return
        }

        val token = args[1]

        if(!IntegrationManager.isValidIntegrationToken(token)) {
            sender.sendMessage(ErrorMessages.invalidTokenSpecified().getMessageWithPrefix())
        }

        val discordID = IntegrationManager.getDiscordIDFromToken(token)
        try {
            val database = SharedPlugin.database
            database.integration().addIntegrationInformation(discordID, sender.uuid)
        } catch (e: Exception) {
            e.printStackTrace()
            sender.sendMessage(Component.text("Something went wrong when integrating your account!"))
            return
        }

        sender.sendMessage(Component.text("Your account has successfully integrated. DiscordID: $discordID, MinecraftUUID:${sender.uuid}"))
    }
}
package com.github.shunsukesudo.minecraft2fa.shared.discord.commands

import com.github.shunsukesudo.minecraft2fa.shared.discord.DiscordBot
import com.github.shunsukesudo.minecraft2fa.shared.integration.IntegrationManager
import dev.creativition.simplejdautil.SimpleJDAUtil
import dev.creativition.simplejdautil.SlashCommandBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button

class IntegrationCommand: ListenerAdapter() {

    companion object {
        private const val commandName = "/mc2fa"
        private const val integrationUnregisterID = "integration-unregister"

        init {
            val cmd = SlashCommandBuilder.createCommand(
                "integration",
                "Integration command"
            )

            val subCmdRegister = SlashCommandBuilder.createSubCommand(
                "register",
                "Start integration between Discord ID and Minecraft UUID"
            )
            val subCmdUnregister =
                SlashCommandBuilder.createSubCommand(
                    "unregister",
                    "Un-Register your integration information"
                )
            val subCmdCheck =
                SlashCommandBuilder.createSubCommand(
                    "check",
                    "Check account is integrated. and integrated into what."
                )

            cmd.addSubCommand(subCmdRegister.build())
            cmd.addSubCommand(subCmdUnregister.build())
            cmd.addSubCommand(subCmdCheck.build())

            SimpleJDAUtil.addSlashCommand(cmd.build())
            SimpleJDAUtil.addListener(IntegrationCommand())
        }
    }

    val database = DiscordBot.getDatabaseConnection()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        // TODO() User discord permission check
        if(event.name != "integration")
            return

        when(event.subcommandName?.lowercase()) {
            "register" -> registerCommandAction(event)
            "unregister" -> unRegisterCommandAction(event)
            "check" -> checkCommandAction(event)
        }
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        when(event.componentId.lowercase()) {
            "$integrationUnregisterID-ready-${event.user.idLong}" -> unRegisterCommandButtonAction(event)
            else -> event.reply("Error: No action found!!!").setEphemeral(true).queue()
        }
    }

    private fun registerCommandAction(event: SlashCommandInteractionEvent) {
        if(database.integration().isIntegrationInformationExists(event.user.idLong)) {
            event.reply("Your account has already integrated with minecraft account!")
                .setEphemeral(true)
                .queue()
            return
        }

        if(IntegrationManager.isIntegrationInProgress(event.user.idLong)) {
            val integrationToken = IntegrationManager.registerIntegrationToken(event.user.idLong)
            event.reply("Integration token renewed. use `$commandName register $integrationToken` in minecraft server to proceed.")
                .setEphemeral(true)
                .queue()
        }
        else {
            val integrationToken = IntegrationManager.registerIntegrationToken(event.user.idLong)
            event.reply("Integration token generated. use `$commandName register $integrationToken` in minecraft server to proceed.")
                .setEphemeral(true)
                .queue()
        }
    }




    private fun unRegisterCommandAction(event: SlashCommandInteractionEvent) {
        if(!database.integration().isIntegrationInformationExists(event.user.idLong)) {
            event.reply("Your account is not integrated with minecraft account!")
                .setEphemeral(true)
                .queue()
            return
        }

        event.reply("Press button to unregister integration.")
            .setEphemeral(true)
            .setActionRow(
                Button.primary("$integrationUnregisterID-ready-${event.user.idLong}",
                "Unregister integration")
            )
            .queue()
    }

    private fun unRegisterCommandButtonAction(event: ButtonInteractionEvent) {
        val userDiscordID = event.user.idLong
        database.integration().removeIntegrationInformation(userDiscordID)

        if(database.integration().isIntegrationInformationExists(userDiscordID)) {
            event.reply("Failed to unregister integration!")
        }
        else {
            event.reply("Your integration information successfully unregistered!")
        }
    }


    private fun checkCommandAction(event: SlashCommandInteractionEvent) {
        val isInProgress = IntegrationManager.isIntegrationInProgress(event.user.idLong)

        if(isInProgress) {
            event.reply("Your integration is currently registering progress.")
                .setEphemeral(true)
                .queue()
            return
        }

        val userDiscordID = event.user.idLong
        val isUserIntegrated = database.integration().isIntegrationInformationExists(userDiscordID)

        if(!isUserIntegrated) {
            event.reply("Your account is not integrated. to integrate, use `/integration register`")
                .setEphemeral(true)
                .queue()
        }

        val userUUID = database.integration().getMinecraftUUIDFromDiscordID(userDiscordID)
        event.reply("Your account is integrated with minecraft account. UUID: $userUUID")
            .setEphemeral(true)
            .queue()
    }
}
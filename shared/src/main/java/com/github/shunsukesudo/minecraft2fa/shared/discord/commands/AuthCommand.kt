package com.github.shunsukesudo.minecraft2fa.shared.discord.commands

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import com.github.shunsukesudo.minecraft2fa.shared.authentication.User2FA
import com.github.shunsukesudo.minecraft2fa.shared.discord.DiscordBot
import com.github.shunsukesudo.minecraft2fa.shared.event.MC2FAEvent
import com.github.shunsukesudo.minecraft2fa.shared.event.auth.AuthSuccessEvent
import com.github.shunsukesudo.minecraft2fa.shared.util.QRCodeUtil
import dev.creativition.simplejdautil.SimpleJDAUtil
import dev.creativition.simplejdautil.SlashCommandBuilder
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import net.dv8tion.jda.api.utils.FileUpload
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

class AuthCommand: ListenerAdapter() {

    companion object {
        var otpIssuerName = "Minecraft2FA"
        private const val verificationRegisterID = "2fa-verification-register"
        private const val verificationUnRegisterID = "2fa-verification-unregister"
        private const val verificationVerifyID = "2fa-verification-verify"

        init {
            val cmd = SlashCommandBuilder.createCommand("auth", "Authentication command")

            val subCmdRegister = SlashCommandBuilder.createSubCommand("register", "Register 2fa authentication")
            val subCmdUnregister = SlashCommandBuilder.createSubCommand("unregister", "Un-Register 2fa authentication")
            val subCmdVerify = SlashCommandBuilder.createSubCommand("verify", "Verify the session")

            cmd.addSubCommand(subCmdRegister.build())
            cmd.addSubCommand(subCmdUnregister.build())
            cmd.addSubCommand(subCmdVerify.build())

            SimpleJDAUtil.addSlashCommand(cmd.build())
            SimpleJDAUtil.addListener(AuthCommand())
        }
    }

    val database = DiscordBot.getDatabaseConnection()

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        // TODO() User discord permission check
        // TODO() Integration check and 2FA registration check
        if(event.name != "auth")
            return

        when(event.subcommandName?.lowercase()) {
            "register" -> registerCommandAction(event)
            "unregister" -> unRegisterCommandAction(event)
            "verify" -> verifyCommandAction(event)
            else -> DiscordBot.replyErrorMessage(event, "Sub command is not supported")
        }
    }


    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        when(event.componentId.lowercase()) {
            "$verificationRegisterID-ready-${event.user.idLong}" -> registerCommandButtonAction(event)
            "$verificationUnRegisterID-ready-${event.user.idLong}" -> unRegisterCommandButtonAction(event)
            else -> event.reply("Error: No action found!!!").setEphemeral(true).queue()
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        when(event.modalId.lowercase()) {
            "$verificationRegisterID-modal-${event.user.idLong}" -> registerCommandModalAction(event)
            "$verificationUnRegisterID-modal-${event.user.idLong}" -> unRegisterCommandModalAction(event)
            "$verificationVerifyID-modal-${event.user.idLong}" -> verifyCommandModalAction(event)
        }
    }



    private fun registerCommandAction(event: SlashCommandInteractionEvent) {
        if(database.authentication().is2FAAuthenticationInformationExists(database.integration().getPlayerID(event.user.idLong))) {
           event.reply("You have already registered the 2FA!").setEphemeral(true).queue()
           return
        }

        val credentials = User2FA.createNewCredentials(event.user.idLong)
        val totpAuthRegistrationURI = "otpauth://totp/Minecraft2FA:${event.member!!.effectiveName}?secret=${credentials.getSecretKey()}&issuer=$otpIssuerName"

        try {
            val image = QRCodeUtil.generateQRCodeFromString(totpAuthRegistrationURI)
            val bos = ByteArrayOutputStream()
            ImageIO.write(image, "png", bos)
            event.reply("Scan QR code with Any TOTP compatible authenticator application or paste secret key to application: `${credentials.getSecretKey()}`.")
                .addFiles(FileUpload.fromData(bos.toByteArray(), "2FA-QR.png"))
                .setEphemeral(true)
                .setActionRow(Button.primary("$verificationRegisterID-ready-${event.user.idLong}", "Verify Code"))
                .queue()
        } catch (e: Exception) {
            e.printStackTrace()
            event.reply("Failed to generate QR code! Please try again.")
                .setEphemeral(true)
                .queue()
        }
    }

    private fun registerCommandButtonAction(event: ButtonInteractionEvent) {
        sendModal(event, verificationRegisterID)
    }

    private fun registerCommandModalAction(event: ModalInteractionEvent) {
        val inputCode = event.getValue("$verificationRegisterID-input-${event.user.idLong}")?.asString?.toInt()
        if(inputCode == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get verification code!")
            return
        }

        val credential = User2FA.getCredentials(event.user.idLong)
        if(credential == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get credential!")
            return
        }
        val secretKey = credential.getSecretKey()
        val backUpCodes = credential.getBackupCodes()

        if (User2FA.authorize(secretKey, inputCode)) {
            database.authentication().add2FAAuthenticationInformation(
                database.integration().getPlayerID(event.user.idLong),
                secretKey,
                backUpCodes
            )

            event.reply("Your 2FA registered successfully!!").setEphemeral(true).queue()
        }
        else {
            event.reply("Invalid code! Please try again.").setEphemeral(true).queue()
        }
    }



    private fun unRegisterCommandAction(event: SlashCommandInteractionEvent) {
        val secretKey = database.authentication().get2FASecretKey(database.integration().getPlayerID(event.user.idLong))
        if(secretKey == null) {
            DiscordBot.replyErrorMessage(event, "Seems you haven't registered the 2FA. Please register 2FA first.")
            return
        }

        event.reply("Press button and input code to unregister the your 2FA!")
            .setEphemeral(true)
            .setActionRow(Button.primary("$verificationUnRegisterID-ready-${event.user.idLong}", "Verify Code"))
            .queue()
    }

    private fun unRegisterCommandButtonAction(event: ButtonInteractionEvent) {
        sendModal(event, verificationUnRegisterID)
    }

    private fun unRegisterCommandModalAction(event: ModalInteractionEvent) {
        val inputCode = event.getValue("$verificationUnRegisterID-input-${event.user.idLong}")?.asString?.toInt()
        if(inputCode == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get verification code!")
            return
        }

        val userID = database.integration().getPlayerID(event.user.idLong)
        val secretKey = database.authentication().get2FASecretKey(userID)
        if(secretKey == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get secret key from database!")
            return
        }

        if (!User2FA.authorize(secretKey, inputCode)) {
            event.reply("Invalid code! Please try again.").setEphemeral(true).queue()
            return
        }

        when(database.authentication().remove2FAAuthenticationInformation(userID)) {
            0 -> DiscordBot.replyErrorMessage(event, "There is no 2FA information in database.")
            else -> event.reply("Your 2FA unregistered successfully!!").setEphemeral(true).queue()
        }
    }



    private fun verifyCommandAction(event: SlashCommandInteractionEvent) {
        val secretKey = database.authentication().get2FASecretKey(database.integration().getPlayerID(event.user.idLong))
        if(secretKey == null) {
            DiscordBot.replyErrorMessage(event, "Seems you haven't registered the 2FA. Please register 2FA first.")
            return
        }

        val uuid = UUID.fromString(database.integration().getMinecraftUUIDFromDiscordID(event.user.idLong))

        if(MCUserAuth.isUserAuthorized(uuid)) {
            event.reply("You are already in verified session! session will expire in {}")
            return
        }

        sendModal(event, verificationVerifyID)
    }

    private fun verifyCommandModalAction(event: ModalInteractionEvent) {
        val inputCode = event.getValue("$verificationVerifyID-input-${event.user.idLong}")?.asString?.toInt()
        if(inputCode == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get verification code!")
            return
        }

        val userID = database.integration().getPlayerID(event.user.idLong)
        val secretKey = database.authentication().get2FASecretKey(userID)
        if(secretKey == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get secret key from database!")
            return
        }

        if (!User2FA.authorize(secretKey, inputCode)) {
            event.reply("Invalid code! Please try again.").setEphemeral(true).queue()
            return
        }

        MC2FAEvent.callEvent(AuthSuccessEvent(event.user.idLong))
    }


    private fun sendModal(event: ButtonInteractionEvent, id: String) {
        val totpInput = getTextInput("$id-input-${event.user.idLong}")
        val modal = getModal("$id-modal-${event.user.idLong}", totpInput)

        event.replyModal(modal).queue()
    }

    private fun sendModal(event: SlashCommandInteractionEvent, id: String) {
        val totpInput = getTextInput("$id-input-${event.user.idLong}")
        val modal = getModal("$id-modal-${event.user.idLong}", totpInput)

        event.replyModal(modal).queue()
    }

    private fun getTextInput(id: String): TextInput {
        return TextInput.create(id, "2FA code", TextInputStyle.SHORT)
            .setMinLength(6)
            .setMaxLength(6)
            .setRequired(true)
            .build()
    }

    private fun getModal(id: String, textInput: TextInput): Modal {
        return Modal.create(id, "Enter 2FA code shown in your authenticator!")
            .addComponents(ActionRow.of(textInput))
            .build()
    }
}
package com.github.shunsukesudo.minecraft2fa.shared.discord.commands

import com.github.shunsukesudo.minecraft2fa.shared.authentication.User2FAAuthentication
import com.github.shunsukesudo.minecraft2fa.shared.discord.DiscordBot
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
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
import javax.imageio.ImageIO

class AuthCommand: ListenerAdapter() {

    companion object {
        var otpIssuerName = "Minecraft2FA"
        private const val verificationRegisterID = "2fa-verification-register"
        private const val verificationUnRegisterID = "2fa-verification-unregister"

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
        }
    }



    private fun registerCommandAction(event: SlashCommandInteractionEvent) {
        val credentials = User2FAAuthentication.createNewCredentials(event.user.idLong)
        val totpAuthRegistrationURI = "otpauth://totp/Minecraft2FA:${event.member!!.effectiveName}?secret=${credentials.getSecretKey()}&issuer=$otpIssuerName"

        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(totpAuthRegistrationURI, BarcodeFormat.QR_CODE, 256, 256)
            val image = MatrixToImageWriter.toBufferedImage(bitMatrix)
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
        val totpInput = TextInput.create("$verificationRegisterID-input-${event.user.idLong}", "2FA code", TextInputStyle.SHORT)
            .setMinLength(6)
            .setMaxLength(6)
            .setRequired(true)
            .build()

        val modal = Modal.create("$verificationRegisterID-modal-${event.user.idLong}", "Enter 2FA code shown in your authenticator!")
            .addComponents(ActionRow.of(totpInput))
            .build()

        event.replyModal(modal).queue()
    }

    private fun registerCommandModalAction(event: ModalInteractionEvent) {
        val inputCode = event.getValue("$verificationRegisterID-modal-${event.user.idLong}")?.asString?.toInt()
        if(inputCode == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get verification code!")
            return
        }

        val secretKey = User2FAAuthentication.getCredentials(event.user.idLong)?.getSecretKey()
        if(secretKey == null) {
            DiscordBot.replyErrorMessage(event, "Failed to get secret key!")
            return
        }

        if (User2FAAuthentication.authorize(secretKey, inputCode)) {
            database.authentication().add2FAAuthenticationInformation(
                database.integration().getPlayerID(event.user.idLong),
                secretKey
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
            DiscordBot.replyErrorMessage(event, "Secret key is not found in database. Please ask to server administrator.")
            return
        }

        event.reply("Press button and input code to unregister the your 2FA!")
            .setEphemeral(true)
            .setActionRow(Button.primary("$verificationUnRegisterID-ready-${event.user.idLong}", "Verify Code"))
            .queue()
    }

    private fun unRegisterCommandButtonAction(event: ButtonInteractionEvent) {

    }

    private fun unRegisterCommandModalAction(event: ModalInteractionEvent) {

    }



    // This command will reply with button message and handler will be implemented in plugin side discord bot.
    private fun verifyCommandAction(event: SlashCommandInteractionEvent) {
    }
}
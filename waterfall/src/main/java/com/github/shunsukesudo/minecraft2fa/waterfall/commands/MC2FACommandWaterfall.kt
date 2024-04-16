package com.github.shunsukesudo.minecraft2fa.waterfall.commands

import com.github.shunsukesudo.minecraft2fa.shared.minecraft.command.MC2FACommand
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.command.SharedCommand
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.command.WaterfallCommand

class MC2FACommandWaterfall(override val sharedCommand: SharedCommand = MC2FACommand()): WaterfallCommand(sharedCommand)
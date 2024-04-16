package com.github.shunsukesudo.minecraft2fa.paper.commands

import com.github.shunsukesudo.minecraft2fa.shared.minecraft.command.MC2FACommand
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.command.PaperCommand
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.command.SharedCommand

class MC2FACommandPaper(override val sharedCommand: SharedCommand = MC2FACommand()) : PaperCommand()
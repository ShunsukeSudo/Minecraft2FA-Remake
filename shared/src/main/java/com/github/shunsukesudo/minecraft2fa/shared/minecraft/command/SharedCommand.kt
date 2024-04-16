package com.github.shunsukesudo.minecraft2fa.shared.minecraft.command

import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayer

interface SharedCommand {
    val commandName: String
    val commandAliases: Array<String>
    val permission: String
    fun execute(sender: SharedPlayer, args: Array<String>)
    fun suggest(sender: SharedPlayer, args: Array<String>): List<String>
}
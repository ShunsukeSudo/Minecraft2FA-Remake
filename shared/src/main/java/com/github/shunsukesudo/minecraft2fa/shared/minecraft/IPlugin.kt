package com.github.shunsukesudo.minecraft2fa.shared.minecraft

import com.github.shunsukesudo.minecraft2fa.shared.configuration.PluginConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase
import com.github.shunsukesudo.minecraft2fa.shared.minecraft.player.SharedPlayer
import java.util.UUID
import java.util.concurrent.TimeUnit

interface IPlugin {
    val pluginConfiguration: PluginConfiguration
    val database: MC2FADatabase

    fun runTask(runnable: Runnable, delay: Long, unit: TimeUnit)

    fun findPlayer(uuid: UUID): SharedPlayer?
}
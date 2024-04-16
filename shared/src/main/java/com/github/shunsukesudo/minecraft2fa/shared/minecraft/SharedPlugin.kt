package com.github.shunsukesudo.minecraft2fa.shared.minecraft

import com.github.shunsukesudo.minecraft2fa.shared.configuration.PluginConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase

object SharedPlugin {
    lateinit var plugin: IPlugin
    val pluginConfiguration: PluginConfiguration
        get() = plugin.pluginConfiguration
    val database: MC2FADatabase
        get() = plugin.database
}
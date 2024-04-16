package com.github.shunsukesudo.minecraft2fa.shared.minecraft

import com.github.shunsukesudo.minecraft2fa.shared.configuration.PluginConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.database.MC2FADatabase

interface IPlugin {
    val pluginConfiguration: PluginConfiguration
    val database: MC2FADatabase
}
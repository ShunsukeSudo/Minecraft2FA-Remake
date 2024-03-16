package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration

interface MC2FADatabase {

    /**
     * Returns authentication class
     */
    fun getAuthentication(): Authentication

    /**
     * Returns integration class
     */
    fun getIntegration(): Integration
}
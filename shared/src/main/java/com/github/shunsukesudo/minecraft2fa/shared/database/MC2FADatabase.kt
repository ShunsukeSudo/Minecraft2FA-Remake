package com.github.shunsukesudo.minecraft2fa.shared.database

import com.github.shunsukesudo.minecraft2fa.shared.database.auth.Authentication
import com.github.shunsukesudo.minecraft2fa.shared.database.integration.Integration
import org.jetbrains.exposed.sql.Database

interface MC2FADatabase {

    /**
     * Returns authentication class
     */
    fun authentication(): Authentication

    /**
     * Returns integration class
     */
    fun integration(): Integration

    /**
     * Returns database connection
     */
    fun getDatabaseConnection(): Database
}
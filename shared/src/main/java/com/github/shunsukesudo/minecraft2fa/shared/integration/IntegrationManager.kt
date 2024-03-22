package com.github.shunsukesudo.minecraft2fa.shared.integration

object IntegrationManager {
    private val tokenMap = HashMap<Long, String>()

    /**
     *
     * Checks integration token is valid.
     *
     * @param token Integration token
     * @return true if valid, otherwise false
     */
    fun isValidIntegrationToken(token: String): Boolean {
        return tokenMap.containsValue(token)
    }

    /**
     *
     * Register new integration token.
     *
     * @throws IllegalArgumentException When negative long in discordID
     * @param discordID Discord ID
     * @return Register integration token
     */
    fun registerIntegrationToken(discordID: Long): String {
        if(discordID < 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val len = 12
        val token = (1..len).map { alphabet.random() }.joinToString("")
        tokenMap[discordID] = token
        return token
    }

    /**
     *
     * Removes token from manager. This should be call after integration completed/cancelled
     *
     * @throws IllegalArgumentException When negative long in discordID
     * @param discordID Discord ID
     */
    fun removeIntegrationToken(discordID: Long) {
        if(discordID < 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        tokenMap[discordID] = ""
    }

    /**
     *
     * Checks integration is in progress from discord.
     *
     * @throws IllegalArgumentException When negative long in discordID
     * @param discordID DiscordID
     * @return true if token is not null or empty, otherwise false
     */
    fun isIntegrationInProgress(discordID: Long): Boolean {
        if(discordID < 0)
            throw IllegalArgumentException("Discord user ID must be positive long!")

        return !tokenMap[discordID].isNullOrEmpty()
    }

    /**
     *
     * Retrieves discord from integration token.
     *
     * @param token Integration token
     * @return Discord ID
     */
    fun getDiscordIDFromToken(token: String): Long {
        return tokenMap.filterValues { it == token }.keys.first()
    }
}
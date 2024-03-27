package com.github.shunsukesudo.minecraft2fa.shared.authentication

import java.util.UUID

object MCUserAuth {

    private val authorizedUser = HashMap<UUID, MCUserAuthStatus>()

    /**
     *
     * Sets the user authorization status.
     *
     * @param minecraftUUID UUID of Player
     * @param authStatus authorization status
     */
    @JvmStatic
    fun setUserAuthorizationStatus(minecraftUUID: UUID, authStatus: MCUserAuthStatus) {
        authorizedUser[minecraftUUID] = authStatus
    }

    /**
     *
     * Checks user is authorized.
     *
     * @param minecraftUUID UUID of Player
     * @return Returns MCUserAuthStatus enum
     */
    @JvmStatic
    fun getUserAuthorizationStatus(minecraftUUID: UUID): MCUserAuthStatus{
        return authorizedUser[minecraftUUID] ?: MCUserAuthStatus.NOT_AUTHORIZED
    }
}
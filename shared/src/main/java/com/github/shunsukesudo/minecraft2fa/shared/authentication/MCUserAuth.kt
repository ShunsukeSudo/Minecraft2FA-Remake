package com.github.shunsukesudo.minecraft2fa.shared.authentication

import java.util.UUID

object MCUserAuth {

    private val authorizedUser = HashMap<UUID, Boolean>()

    /**
     *
     * Sets the user authorization status.
     *
     * @param minecraftUUID UUID of Player
     * @param isAuthorized authorization status
     */
    @JvmStatic
    fun setUserAuthorizedStatus(minecraftUUID: UUID ,isAuthorized: Boolean) {
        authorizedUser[minecraftUUID] = isAuthorized
    }

    /**
     *
     * Checks user is authorized.
     *
     * @param minecraftUUID UUID of Player
     * @return true if authorized, otherwise false
     */
    @JvmStatic
    fun isUserAuthorized(minecraftUUID: UUID): Boolean{
        // Return value of `authorizedUser[key]` is `Boolean?` not `Boolean` and need a null check so use when.
        return when(authorizedUser[minecraftUUID]) {
            true -> true
            else -> false
        }
    }
}
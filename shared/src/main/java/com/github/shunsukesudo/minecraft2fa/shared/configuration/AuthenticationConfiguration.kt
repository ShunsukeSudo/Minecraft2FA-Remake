package com.github.shunsukesudo.minecraft2fa.shared.configuration

data class AuthenticationConfiguration(
    val sessionExpireTimeInSeconds: Int,
) {
    init {
        if(sessionExpireTimeInSeconds < 0)
            throw IllegalArgumentException("sessionExpireTimeInSeconds should not be negative value! $sessionExpireTimeInSeconds")

        if(sessionExpireTimeInSeconds == 0)
            throw IllegalArgumentException("sessionExpireTimeInSeconds should greater than 1!")
    }
}
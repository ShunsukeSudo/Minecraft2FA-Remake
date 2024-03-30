package com.github.shunsukesudo.minecraft2fa.shared.configuration

data class DatabaseConfiguration(
    val databaseType: DatabaseType,
    val address: String,
    val user: String,
    val password: String
) {
    init {
        if(address.isEmpty())
            throw IllegalArgumentException("Address is should not be empty.")

        when(databaseType) {
            DatabaseType.MYSQL -> {
                if(user.isEmpty()) {
                    throw IllegalArgumentException("User is should not be empty in $databaseType database")
                }
            }

            DatabaseType.SQLITE -> {
            }

            else -> {
                throw IllegalArgumentException("Unknown database type specified. Type: $databaseType")
            }
        }
    }
}
package com.github.shunsukesudo.minecraft2fa.shared.configuration

data class DatabaseConfiguration(
    val databaseType: DatabaseType,
    val address: String,
    val user: String,
    val password: String
) {
    init {
        when(databaseType) {
            DatabaseType.MYSQL -> {
                if(address.isEmpty()) {
                    throw IllegalArgumentException("Address is should not be empty.")
                }

                if(user.isEmpty()) {
                    throw IllegalArgumentException("User is should not be empty in $databaseType database")
                }
            }

            DatabaseType.SQLITE -> {
                if(address.isEmpty()) {
                    throw IllegalArgumentException("Address is should not be empty.")
                }
            }

            else -> {
                throw IllegalArgumentException("Unknown database type specified. Type: $databaseType")
            }
        }
    }
}
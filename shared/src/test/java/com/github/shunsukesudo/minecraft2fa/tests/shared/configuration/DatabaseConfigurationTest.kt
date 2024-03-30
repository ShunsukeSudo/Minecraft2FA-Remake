package com.github.shunsukesudo.minecraft2fa.tests.shared.configuration

import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseConfiguration
import com.github.shunsukesudo.minecraft2fa.shared.configuration.DatabaseType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DatabaseConfigurationTest {

    @Test
    fun `Test - should throw exception when DatabaseType is MYSQL and user name is empty`() {
        println("=========== Test - should throw exception when DatabaseType is MYSQL and user name is empty")

        val databaseType = DatabaseType.MYSQL
        val address = "address.adr:1000"
        val userName = ""
        val password = ""

        println("Constructing DatabaseConfiguration with these values.")
        println("databaseType: $databaseType, address: $address, user: $userName, password: $password")

        assertThrows<IllegalArgumentException> {
            println("Should throw IllegalArgumentException because user name is empty")
            val sut = DatabaseConfiguration(databaseType, address, userName, password)
        }

        println("Passed.")
    }

    @Test
    fun `Test - should throw exception when address is empty`() {
        println("=========== Test - should throw exception when address is empty")

        val databaseType = DatabaseType.MYSQL
        val address = ""
        val userName = "UserName"
        val password = "PasSWorD"

        println("Constructing DatabaseConfiguration with these values.")
        println("databaseType: $databaseType, address: $address, user: $userName, password: $password")

        assertThrows<IllegalArgumentException> {
            println("Should throw IllegalArgumentException because address is empty")
            val sut = DatabaseConfiguration(databaseType, address, userName, password)
        }

        println("Passed.")
    }

    @Test
    fun `Test - should return expected value after instanced`() {
        println("=========== Test - should return expected value after instanced")

        val databaseType = DatabaseType.SQLITE
        val address = "Address:1000"
        val userName = "UserName"
        val password = "PasSWorD"

        println("Constructing DatabaseConfiguration with these values.")
        println("databaseType: $databaseType, address: $address, user: $userName, password: $password")

        val sut = DatabaseConfiguration(databaseType, address, userName, password)

        println("Expected databaseType: $databaseType | Actual databaseType: ${sut.databaseType}")
        Assertions.assertEquals(databaseType, sut.databaseType)

        println("Expected address: $address | Actual address: ${sut.address}")
        Assertions.assertEquals(address, sut.address)

        println("Expected user name: $userName | Actual user name: ${sut.userName}")
        Assertions.assertEquals(userName, sut.userName)

        println("Expected address: $password | Actual address: ${sut.password}")
        Assertions.assertEquals(password, sut.password)

        println("Passed.")
    }
}
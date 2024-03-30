package com.github.shunsukesudo.minecraft2fa.tests.shared.configuration

import com.github.shunsukesudo.minecraft2fa.shared.configuration.DiscordBotConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DiscordBotConfigurationTest {

    @Test
    fun `Test - should throw exception when bot token is empty`() {
        println("=========== Test - should throw exception when bot token is empty")

        println("Instancing DiscordBotConfiguration with empty string")
        assertThrows<IllegalArgumentException> {
            val sut = DiscordBotConfiguration("")
        }

        println("Passed.")
    }

    @Test
    fun `Test - should return expected value after instanced`() {
        println("=========== Test - should return expected value after instanced")

        val alphabet = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val len = 32
        val expectedValue = (1..len).map { alphabet.random() }.joinToString("")

        println("Instancing with token: $expectedValue")
        val sut = DiscordBotConfiguration(expectedValue)

        val actualValue = sut.botToken

        println("Expected value: $expectedValue | Actual value: $actualValue")
        Assertions.assertEquals(expectedValue, actualValue)
        println("Passed.")
    }
}
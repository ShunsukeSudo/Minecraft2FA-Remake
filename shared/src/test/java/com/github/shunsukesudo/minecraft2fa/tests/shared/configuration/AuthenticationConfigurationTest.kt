package com.github.shunsukesudo.minecraft2fa.tests.shared.configuration

import com.github.shunsukesudo.minecraft2fa.shared.configuration.AuthenticationConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.random.Random

class AuthenticationConfigurationTest {

    private val random = Random(UUID.randomUUID().toString().filter { it.isDigit() }.take(16).toLong())
    
    @Test
    fun `Test - should throw IllegalArgumentException when construct with negative value`() {
        println("=========== Test - should throw IllegalArgumentException when construct with negative value")
        assertThrows<IllegalArgumentException> {
            var randomInt = random.nextInt()
            if(randomInt > 0) randomInt = randomInt.inv()
            println("Constructing with generated random negative int: $randomInt")
            println("Checking constructor of AuthenticationConfiguration throw exception")
            val sut = AuthenticationConfiguration(randomInt)
        }
        
        println("Passed.")
    }

    @Test
    fun `Test - should throw IllegalArgumentException when construct with 0`() {
        println("=========== Test - should throw IllegalArgumentException when construct with 0")
        assertThrows<IllegalArgumentException> {
            val expectedInt = 0
            println("Constructing with $expectedInt")
            println("Checking constructor of AuthenticationConfiguration throw exception")
            val sut = AuthenticationConfiguration(expectedInt)
        }

        println("Passed.")
    }
    
    @Test
    fun `Test - should return actual value after instanced`() {
        println("=========== Test - should return actual value after instanced")
        var expectedValue = random.nextInt()
        if(expectedValue < 0) expectedValue = expectedValue.inv()
        println("Expected value: $expectedValue")

        println("Instancing AuthenticationConfiguration")
        val sut = AuthenticationConfiguration(expectedValue)

        val actualValue = sut.sessionExpireTimeInSeconds
        println("Actual value from instanced AuthenticationConfiguration: $actualValue")

        println("Expected value: $expectedValue | Actual value: $actualValue")
        Assertions.assertEquals(expectedValue, actualValue)
        
        println("Passed")
    }
}
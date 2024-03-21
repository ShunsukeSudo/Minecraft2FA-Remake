package com.github.shunsukesudo.minecraft2fa.tests.shared.authentication

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuthentication
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class MCUserAuthenticationTest {

    @Test
    fun `Test when uninitialized value accessed`() {
        println("=========== Test when uninitialized value accessed")
        val expectedValue = false
        println("Calling: MCUserAuthentication.isUserAuthorized()")
        val actualValue = MCUserAuthentication.isUserAuthorized(UUID.randomUUID())
        println("Expected value: $expectedValue | Actual value: $actualValue")
        Assertions.assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `Test when initialized value accessed`() {
        println("=========== Test when initialized value accessed")
        val expectedValue = true
        val uuid = UUID.randomUUID()
        println("Generated UUID: $uuid")
        println("Calling: MCUserAuthentication.setUserAuthorizedStatus()")
        MCUserAuthentication.setUserAuthorizedStatus(uuid, true)
        val actualValue = MCUserAuthentication.isUserAuthorized(uuid)
        println("Expected value: $expectedValue | Actual value: $actualValue")
        Assertions.assertEquals(expectedValue, actualValue)
    }
}
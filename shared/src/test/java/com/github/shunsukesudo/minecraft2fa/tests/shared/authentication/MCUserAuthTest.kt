package com.github.shunsukesudo.minecraft2fa.tests.shared.authentication

import com.github.shunsukesudo.minecraft2fa.shared.authentication.MCUserAuth
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

class MCUserAuthTest {

    private val random = Random(UUID.randomUUID().toString().filter { it.isDigit() }.take(16).toLong())

    @Test
    fun `Test - Should return false when uninitialized value accessed`() {
        println("=========== Test - Should return false when uninitialized value accessed")
        val expectedValue = false

        println("Calling: MCUserAuth.isUserAuthorized()")
        val actualValue = MCUserAuth.isUserAuthorized(UUID.randomUUID())

        println("Expected value: $expectedValue | Actual value: $actualValue")
        Assertions.assertEquals(expectedValue, actualValue)

        println("Passed.")
    }

    @Test
    fun `Test - Should return individual value when initialized value accessed`() {
        println("=========== Test - Should return individual value when initialized value accessed")

        val uuids: MutableList<UUID> = mutableListOf()
        val expectedValues: HashMap<UUID, Boolean> = HashMap()

        for(i in 1..5) {
            uuids.add(UUID.randomUUID())
        }

        var booleanVal: Boolean

        uuids.forEach {
            booleanVal = random.nextBoolean()
            println("Generated UUID: $it")
            println("Calling: MCUserAuth.setUserAuthorizedStatus() for user: $it")
            MCUserAuth.setUserAuthorizedStatus(it, booleanVal)
            expectedValues[it] = booleanVal
        }

        var actualValue: Boolean
        var expectedValue: Boolean

        uuids.forEach {
            actualValue = MCUserAuth.isUserAuthorized(it)
            expectedValue = expectedValues[it]!!
            println("Expected value: $expectedValue | Actual value: $actualValue")
            Assertions.assertEquals(expectedValue, actualValue)
        }

        println("Passed.")
    }
}
package com.github.shunsukesudo.minecraft2fa.tests.shared.integration

import com.github.shunsukesudo.minecraft2fa.shared.integration.IntegrationManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.random.Random

class IntegrationManagerTest {

    companion object {
        private val integration = IntegrationManager
        private val randomID = Random(UUID.randomUUID().toString().filter { it.isDigit() }.take(16).toLong())
    }


    @Test
    fun `Test generate token and checks token are valid`() {
        println("=========== Test generate token and checks token are valid")

        var id = randomID.nextLong()
        if(id < 0) id = id.inv()
        println("id: $id")

        val token = integration.registerIntegrationToken(id)
        println("token: $token")

        Assertions.assertEquals(true, integration.isValidIntegrationToken(token))

        println("Passed.")
    }

    @Test
    fun `Test get discordID from token`() {
        println("=========== Test get discordID from token")

        var id = randomID.nextLong()
        if(id < 0) id = id.inv()
        println("id: $id")

        val token = integration.registerIntegrationToken(id)
        println("token: $token")

        val actual = integration.getDiscordIDFromToken(token)
        println("Expected id: $id | Actual: $actual")

        Assertions.assertEquals(true, id == actual)

        println("Passed.")
    }

    @Test
    fun `Test integration progress check - Integrating`() {
        println("=========== Test integration progress check - Integrating")

        var id = randomID.nextLong()
        if(id < 0) id = id.inv()
        println("id: $id")

        integration.registerIntegrationToken(id)
        Assertions.assertEquals(true, integration.isIntegrationInProgress(id))

        println("Passed.")
    }

    @Test
    fun `Test integration progress check - Not integrating`() {
        println("=========== Test integration progress check - Not integrating")

        var id = randomID.nextLong()
        if(id < 0) id = id.inv()
        println("id: $id")

        Assertions.assertEquals(false, integration.isIntegrationInProgress(id))

        println("Passed.")
    }

    @Test
    fun `Test integration progress check - After integration cancelled`() {
        println("=========== Test integration progress check - After integration cancelled")

        var id = randomID.nextLong()
        if(id < 0) id = id.inv()
        println("id: $id")

        integration.registerIntegrationToken(id)
        integration.removeIntegrationToken(id)

        Assertions.assertEquals(false, integration.isIntegrationInProgress(id))

        println("Passed.")
    }

    @Test
    fun `Test When invalid discordID entered`() {
        println("=========== Test When invalid discordID entered")

        var id = randomID.nextLong()
        if(id > 0) id = id.inv()
        println("id: $id")

        println("Test registerIntegrationToken() throws exception correctly when discordID is negative long")
        assertThrows<IllegalArgumentException> {
            integration.registerIntegrationToken(id)
        }
        println("Passed: registerIntegrationToken()")

        println("Test removeIntegrationToken() throws exception correctly when discordID is negative long")
        assertThrows<IllegalArgumentException> {
            integration.removeIntegrationToken(id)
        }
        println("Passed: removeIntegrationToken()")
        println("Passed.")

    }

}
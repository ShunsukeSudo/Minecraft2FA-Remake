package com.github.shunsukesudo.minecraft2fa.tests.shared.event

import com.github.shunsukesudo.minecraft2fa.shared.event.EventListener
import com.github.shunsukesudo.minecraft2fa.shared.event.EventHandler
import com.github.shunsukesudo.minecraft2fa.shared.event.GenericEvent
import com.github.shunsukesudo.minecraft2fa.shared.event.MC2FAEvent
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach

class EventTest {

    @BeforeEach
    fun settingUp() {
        isEventExecuted = false
        actualTestValue = ""
        actualTestConstructorValue = ""
    }

    @Test
    fun `Test event value`() {
        val expectedTestValue = "value"
        val expectedTestConstructorValue = "ConstructorValue"

        val event = MC2FATestEvent(expectedTestConstructorValue)


        println("Calling event")
        println("executed? $isEventExecuted")
        MC2FAEvent.callEvent(event)
        println("executed? $isEventExecuted")

        println("Expected: $expectedTestValue | Actual: $actualTestValue")
        Assertions.assertEquals(expectedTestValue, actualTestValue)
        println("Expected: $expectedTestConstructorValue | Actual: $actualTestConstructorValue")
        Assertions.assertEquals(expectedTestConstructorValue, actualTestConstructorValue)


        println("Passed.")
    }

    companion object {
        var actualTestValue = ""
        var actualTestConstructorValue = ""
        var isEventExecuted = false

        @JvmStatic
        @BeforeAll
        fun setUp() {
            val listener = MC2FATestEventListener()
            println("Listener class name?: ${listener.javaClass.name}")
            MC2FAEvent.addListener(listener)
        }
    }



    class MC2FATestEvent(
        private val constructorValue: String
    ): GenericEvent {

        private val value = "value"

        fun getValue(): String {
            return value
        }

        fun getConstructorValue(): String {
            return constructorValue
        }
    }

    class MC2FATestEventListener: EventListener {
        @EventHandler
        fun testEventReceived(event: MC2FATestEvent) {
            println("MC2FATestEvent called")
            EventTest.actualTestValue = event.getValue()
            EventTest.actualTestConstructorValue = event.getConstructorValue()
            isEventExecuted = true
        }

    }
}
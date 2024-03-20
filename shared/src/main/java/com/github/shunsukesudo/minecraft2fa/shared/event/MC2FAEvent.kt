package com.github.shunsukesudo.minecraft2fa.shared.event

import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaType

class MC2FAEvent {
    companion object {

        private val listeners = mutableListOf<EventListener>()

        @JvmStatic
        fun <T: GenericEvent> callEvent(event: T) {
            println("Finding events")
            listeners.forEach { listenerClass ->
                listenerClass::class.memberFunctions.forEach classes@{ func ->
                    println("Finding annotated method")
                    if(!func.annotations.contains(EventHandler())) {
                        return@classes
                    }
                    println("Annotated method found!")
                    println("Annotated method name ${func.name}")
                    println("Annotated method parameters count ${func.parameters.count()}")
                    func.parameters.forEach { param ->
                        println("Val name: ${param.name}")
                        println("Type: ${param.type}")
                        println("Check type is same: ${param.type.javaType} | ${event.javaClass.typeName}")
                        if(param.type.javaType == event.javaClass) {
                            println("Event match!")
                            func.call(listenerClass, event)
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun <T: EventListener> addListener(listener: T) {
            listeners.add(listener)
        }

        @JvmStatic
        fun <T: EventListener> removeListener(listener: T) {
            listeners.remove(listener)
        }
    }

}
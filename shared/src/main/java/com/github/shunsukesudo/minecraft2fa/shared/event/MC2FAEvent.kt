package com.github.shunsukesudo.minecraft2fa.shared.event

import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaType

class MC2FAEvent {
    companion object {

        private val listeners = mutableListOf<EventListener>()
        private val eventHandlerAnnotation = EventHandler()

        @JvmStatic
        fun <T: GenericEvent> callEvent(event: T) {
            listeners.forEach { listenerClass ->
                listenerClass::class.memberFunctions.forEach classes@{ func ->
                    if(!func.annotations.contains(eventHandlerAnnotation)) {
                        return@classes
                    }
                    func.parameters.forEach { param ->
                        if(param.type.javaType == event.javaClass || param.type.javaType == GenericEvent::class.java) {
                            func.call(listenerClass, event)
                            return@classes
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
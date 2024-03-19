package com.github.shunsukesudo.minecraft2fa.shared.event

class MC2FAEvent {
    companion object {

        private val listeners = mutableListOf<EventListener>()

        @JvmStatic
        fun <T: GenericEvent> callEvent(event: T) {
            listeners.forEach {
                if(it.javaClass == event.javaClass) {
                    it.onEvent(event)
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
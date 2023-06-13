package ru.aahzbrut.rpggame.event_bus

class EventBus {
    private val listeners = mutableSetOf<GameEventListener>()

    fun addListener(listener: GameEventListener) {
        listeners += listener
    }

    fun fire(event: GameEvent) {
        listeners.forEach { it.handle(event) }
    }
}

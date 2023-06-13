package ru.aahzbrut.rpggame.event_bus

fun interface GameEventListener {
    fun handle(event: GameEvent)
}

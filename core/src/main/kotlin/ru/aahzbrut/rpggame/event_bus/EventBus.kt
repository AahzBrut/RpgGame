package ru.aahzbrut.rpggame.event_bus

import kotlin.reflect.KClass

class EventBus  {
    @PublishedApi
    internal val listeners = mutableMapOf<KClass<*>, MutableList<(Any) -> Unit>>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : GameEvent> onEvent(noinline action: (T) -> Unit) {
        val actions = listeners.getOrPut(T::class) { mutableListOf() } as MutableList<(T) -> Unit>
        actions += action
    }

    inline fun <reified T : GameEvent> fire(value: T) {
        listeners[value::class]?.forEach { it(value) }
    }
}

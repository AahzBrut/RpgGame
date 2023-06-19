package ru.aahzbrut.rpggame.event_bus.event

import com.github.quillraven.fleks.Entity
import ru.aahzbrut.rpggame.event_bus.GameEvent

class GotItemEvent(
    val receiver: Entity,
) : GameEvent

package ru.aahzbrut.rpggame.event_bus.event

import com.github.quillraven.fleks.Entity
import ru.aahzbrut.rpggame.event_bus.GameEvent

class EntityDamagedEvent(val entity: Entity) : GameEvent

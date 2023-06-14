package ru.aahzbrut.rpggame.event_bus.event

import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.EffectType
import ru.aahzbrut.rpggame.event_bus.GameEvent

class SoundEffectEvent(
    val model: AnimationModel,
    val effectType: EffectType,
): GameEvent

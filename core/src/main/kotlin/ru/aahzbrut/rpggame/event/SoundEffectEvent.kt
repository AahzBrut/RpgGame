package ru.aahzbrut.rpggame.event

import com.badlogic.gdx.scenes.scene2d.Event
import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.EffectType

class SoundEffectEvent(
    val model: AnimationModel,
    val effectType: EffectType,
): Event()

package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.LootComponent
import ru.aahzbrut.rpggame.data.*
import ru.aahzbrut.rpggame.event_bus.event.LootFoundEvent
import ru.aahzbrut.rpggame.event_bus.event.SoundEffectEvent
import ru.aahzbrut.rpggame.event_bus.EventBus

class LootSystem(
    private val eventBus: EventBus = inject(),
) : IteratingSystem(
    family { all(LootComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        with(entity[LootComponent]) {
            if (lootingEntity == null) return

            entity.configure { it -= LootComponent }

            entity.getOrNull(AnimationComponent)?.run {
                eventBus.fire(LootFoundEvent())
                eventBus.fire(SoundEffectEvent(AnimationModel.CHEST, EffectType.OPEN))
                setAnimation(AnimationId(AnimationModel.CHEST, AnimationType.OPEN, FacingType.NONE))
                playMode = Animation.PlayMode.NORMAL
            }
        }
    }
}

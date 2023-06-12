package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.LootComponent
import ru.aahzbrut.rpggame.data.*
import ru.aahzbrut.rpggame.event.SoundEffectEvent

class LootSystem(
    private val stage: Stage = inject("gameStage")
) : IteratingSystem(
    family { all(LootComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        with(entity[LootComponent]) {
            if (lootingEntity == null) return

            entity.configure { it -= LootComponent }

            entity.getOrNull(AnimationComponent)?.run {
                stage.root.fire(SoundEffectEvent(AnimationModel.CHEST, EffectType.OPEN))
                setAnimation(AnimationId(AnimationModel.CHEST, AnimationType.OPEN, FacingType.NONE))
                playMode = Animation.PlayMode.NORMAL
            }
        }
    }
}

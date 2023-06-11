package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.LootComponent
import ru.aahzbrut.rpggame.data.AnimationId
import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType

class LootSystem : IteratingSystem(
    family { all(LootComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        with(entity[LootComponent]) {
            if (lootingEntity == null) return

            entity.configure { it -= LootComponent }

            entity.getOrNull(AnimationComponent)?.run {
                setAnimation(AnimationId(AnimationModel.CHEST, AnimationType.OPEN, FacingType.NONE))
                playMode = Animation.PlayMode.NORMAL
            }
        }
    }
}

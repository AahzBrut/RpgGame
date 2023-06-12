package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.*
import ru.aahzbrut.rpggame.data.EffectType
import ru.aahzbrut.rpggame.data.UIStyles
import ru.aahzbrut.rpggame.event.SoundEffectEvent

class LifeManagementSystem(
    private val uiStyles: UIStyles = inject(),
    private val stage: Stage = inject("gameStage")
) : IteratingSystem(
    family { all(LifeComponent, PhysicsComponent, AnimationComponent).none(DeathComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        entity[LifeComponent].run {
            currentValue += regenerationValue * deltaTime
            currentValue.coerceAtLeast(maxValue)

            currentValue -= damageValue
            if (damageValue > 0f) entity[PhysicsComponent].run {
                displayDamageText(damageValue, body.position, size)
            }
            damageValue = 0f

            if (isDead) {
                stage.root.fire(SoundEffectEvent(entity[AnimationComponent].model, EffectType.DEATH))
                entity.configure { it += DeathComponent(if (it.has(PlayerComponent)) 7f else 0f) }
            }
        }
    }

    private fun displayDamageText(damageValue: Float, position: Vector2, size: Vector2) {
        world.entity {
            it += FloatingTextComponent().apply {
                @Suppress("kotlin:S6518")
                startLocation.set(position.x - size.x * 0.125f, position.y - size.y * 0.125f)
                lifeSpan = 1.5f
                label = Label(damageValue.toInt().toString(), uiStyles.floatingLabelStyle).apply {
                    setFontScale(2f)
                }
            }
        }
    }
}

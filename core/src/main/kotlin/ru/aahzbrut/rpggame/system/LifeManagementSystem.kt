package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.aahzbrut.rpggame.component.DeathComponent
import ru.aahzbrut.rpggame.component.LifeComponent
import ru.aahzbrut.rpggame.component.PlayerComponent

class LifeManagementSystem : IteratingSystem(
    family { all(LifeComponent).none(DeathComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        entity[LifeComponent].run {
            currentValue += regenerationValue * deltaTime
            currentValue.coerceAtLeast(maxValue)

            currentValue -= damageValue
            damageValue = 0f

            if (isDead) {
                entity.configure { it += DeathComponent(if (it.has(PlayerComponent)) 7f else 0f) }
            }
        }
    }
}

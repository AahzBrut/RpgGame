package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.aahzbrut.rpggame.component.DeathComponent
import ru.aahzbrut.rpggame.component.LifeComponent

class DeathManagementSystem : IteratingSystem(
    family { all(DeathComponent)}
) {
    override fun onTickEntity(entity: Entity) {
        entity[DeathComponent].run {
            if (reviveTime == 0f) {
                entity.remove()
                return
            }

            reviveTime -= deltaTime
            if (reviveTime <= 0){
                entity[LifeComponent].run {
                    currentValue = maxValue
                }
                entity.configure { it -= DeathComponent }
            }

        }
    }
}

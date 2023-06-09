package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.AttackComponent
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent
import ru.aahzbrut.rpggame.data.AttackState

class AttackSystem(
    private val physicsWorld: World = inject()
) : IteratingSystem(
    family { all(AttackComponent, PhysicsComponent, ImageComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        entity[AttackComponent].run {
            if (isReady && !startAttack) return

            if (isPrepared && startAttack) {
                startAttack = false
                state = AttackState.ATTACK
                delay = maxDelay
                return
            }

            delay -= deltaTime
            if (delay <= 0 && isAttacking) {
                state = AttackState.DEAL_DAMAGE
            }
        }
    }
}

package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.box2d.query
import ktx.math.plus
import ktx.math.vec2
import ru.aahzbrut.rpggame.component.*
import ru.aahzbrut.rpggame.data.AttackState
import ru.aahzbrut.rpggame.entity

class AttackSystem(
    private val physicsWorld: World = inject()
) : IteratingSystem(
    family { all(AttackComponent, PhysicsComponent, AnimationComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        entity[AttackComponent].let {
            if (it.isReady && !it.startAttack) return

            if (it.isPrepared && it.startAttack) {
                it.startAttack = false
                it.state = AttackState.ATTACK
                it.delay = it.maxDelay
                return
            }

            it.delay -= deltaTime
            if (it.delay <= 0 && it.isAttacking) {
                doAttack(it, entity)
            }
        }
    }

    private fun doAttack(it: AttackComponent, entity: Entity) {
        it.state = AttackState.DEAL_DAMAGE

        val animationComponent = entity[AnimationComponent]
        val physicsComponent = entity[PhysicsComponent]
        val attackDirection = animationComponent.direction
        val position = physicsComponent.body.position
        val size = physicsComponent.size
        val attackCenter = vec2().set(position).plus(vec2().set(attackDirection).scl(size))

        it.attackAreaCenter.set(attackCenter)
        it.attackAreaSize.set(size)

        physicsWorld.query(
            attackCenter.x - size.x * 0.5f,
            attackCenter.y - size.y * 0.5f,
            attackCenter.x + size.x * 0.5f,
            attackCenter.y + size.y * 0.5f
        ) { fixture ->
            if (fixture.isSensor) return@query true
            if (entity == fixture.entity) return@query true
            if (fixture.entity.has(LifeComponent)) {
                fixture.entity[LifeComponent].damageValue += it.damage * MathUtils.random(0.9f, 1.1f)
            }
            true
        }

        it.state = AttackState.READY
    }
}

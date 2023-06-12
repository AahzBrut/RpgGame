package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.box2d.query
import ktx.math.plus
import ru.aahzbrut.rpggame.abs
import ru.aahzbrut.rpggame.component.*
import ru.aahzbrut.rpggame.data.AttackState
import ru.aahzbrut.rpggame.data.EffectType
import ru.aahzbrut.rpggame.entity
import ru.aahzbrut.rpggame.event.SoundEffectEvent

class AttackSystem(
    private val physicsWorld: World = inject(),
    private val stage: Stage = inject("gameStage")
) : IteratingSystem(
    family { all(AttackComponent, PhysicsComponent, AnimationComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        entity[AttackComponent].let {
            if (it.isReady && !it.startAttack) return

            if (it.isPrepared && it.startAttack) {
                it.startAttack = false
                it.state = AttackState.ATTACK
                stage.root.fire(SoundEffectEvent(entity[AnimationComponent].model, EffectType.ATTACK))
                return
            }

            if (it.delay > 0) {
                it.delay -= deltaTime
            }

            if (entity[AnimationComponent].isDone) it.state = AttackState.READY

            if (it.delay <= 0 && it.isAttacking) {
                doAttack(it, entity)
                it.delay = it.maxDelay
            }
        }
    }

    private fun doAttack(it: AttackComponent, entity: Entity) {
        it.state = AttackState.DEAL_DAMAGE

        val animationComponent = entity[AnimationComponent]
        val physicsComponent = entity[PhysicsComponent]
        val attackDirection = animationComponent.direction
        val position = physicsComponent.body.position
        val entitySize = physicsComponent.size
        val attackCenter = position.cpy().plus(attackDirection.cpy().scl(entitySize).scl(0.5f))
        val size = entitySize.cpy().plus(attackDirection.cpy().abs().scl(entitySize))

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

            fixture.entity.getOrNull(LifeComponent)?.run {
                damageValue += it.damage * MathUtils.random(0.9f, 1.1f)
            }

            if (entity has PlayerComponent) {
                fixture.entity.getOrNull(LootComponent)?.run {
                    lootingEntity = entity
                }
            }

            true
        }
    }
}

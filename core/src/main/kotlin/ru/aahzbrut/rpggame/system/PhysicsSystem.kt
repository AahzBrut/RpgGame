package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent

class PhysicsSystem(
    private val physicsWorld: World = inject()
) : IteratingSystem(
    family = family { all(PhysicsComponent, ImageComponent) },
    interval = Fixed(1 / 60f)
) {

    override fun onUpdate() {
        if (physicsWorld.autoClearForces) physicsWorld.autoClearForces = false
        super.onUpdate()
        physicsWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        physicsWorld.step(deltaTime, 6, 2)
    }

    override fun onTickEntity(entity: Entity) {
        val physicsComponent = entity[PhysicsComponent]
        val body = entity[PhysicsComponent].body

        physicsComponent.prevPos.set(body.position)

        if (!physicsComponent.impulse.isZero) {
            body.applyLinearImpulse(physicsComponent.impulse, body.worldCenter, true)
            physicsComponent.impulse.setZero()
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val image = entity[ImageComponent].image
        val physicsComponent = entity[PhysicsComponent]
        val body = entity[PhysicsComponent].body

        body.position.let { newPosition ->
            physicsComponent.prevPos.let { prevPosition ->
                image.run {
                    setPosition(
                        MathUtils.lerp(prevPosition.x, newPosition.x, alpha) - width * 0.5f,
                        MathUtils.lerp(prevPosition.y, newPosition.y, alpha) - height * 0.5f
                    )
                }
            }
        }
    }
}

package ru.aahzbrut.rpggame.system

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
    interval = Fixed(1 / 30f)
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
        val image = entity[ImageComponent].image
        val physicsComponent = entity[PhysicsComponent]
        val body = entity[PhysicsComponent].body

        if (!physicsComponent.impulse.isZero) {
            body.applyLinearImpulse(physicsComponent.impulse, body.worldCenter, true)
            physicsComponent.impulse.setZero()
        }

        body.position.let {
            image.run {
                setPosition(it.x - width * 0.5f, it.y - height * 0.5f)
            }
        }
    }
}

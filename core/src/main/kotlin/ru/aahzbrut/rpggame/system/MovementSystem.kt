package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ktx.math.minus
import ktx.math.unaryMinus
import ktx.math.vec2
import ru.aahzbrut.rpggame.component.MovementComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent

class MovementSystem : IteratingSystem(
    family { all(MovementComponent, PhysicsComponent) }
) {

    override fun onTickEntity(entity: Entity) {
        entity[MovementComponent].let {
            entity[PhysicsComponent].run {
                if (it.direction.epsilonEquals(vec2())) {
                    val negatingVelocity = vec2()
                        .set(body.linearVelocity)
                        .unaryMinus()
                        .scl(body.mass)
                    impulse.set(negatingVelocity)
                    return
                } else {
                    val newVelocity = vec2()
                        .set(it.direction)
                        .scl(it.speed)
                        .minus(body.linearVelocity)
                        .scl(body.mass)
                    impulse.set(newVelocity)
                }
            }
        }
    }
}

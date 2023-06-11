package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ktx.math.minus
import ktx.math.unaryMinus
import ktx.math.vec2
import ru.aahzbrut.rpggame.ai.DefaultState
import ru.aahzbrut.rpggame.component.MovementComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent
import ru.aahzbrut.rpggame.component.StateComponent

class MovementSystem : IteratingSystem(
    family { all(MovementComponent, PhysicsComponent, StateComponent) }
) {

    override fun onTickEntity(entity: Entity) {
        entity[MovementComponent].let {
            if (!entity[StateComponent].stateMachine.isInState(DefaultState.RUN)) {
                it.direction.setZero()
            }
            entity[PhysicsComponent].run {
                if (it.direction.epsilonEquals(Vector2.Zero)) {
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

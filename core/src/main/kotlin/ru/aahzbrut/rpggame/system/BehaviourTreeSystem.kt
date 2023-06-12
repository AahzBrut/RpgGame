package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ktx.math.minus
import ktx.math.vec2
import ru.aahzbrut.rpggame.component.*

class BehaviourTreeSystem : IteratingSystem(
    family { all(BehaviourTreeComponent, PhysicsComponent).none(DeathComponent) }
) {
    private val position = vec2()
    private val playerPosition = vec2()
    private val aggroDistanceSqr = 16f

    override fun onTickEntity(entity: Entity) {
        with(entity[BehaviourTreeComponent]) {
            nearbyEntities.clear()
            position.set(entity[PhysicsComponent].body.position)
            world.family { all(PlayerComponent, PhysicsComponent).none(DeathComponent) }.forEach {
                playerPosition.set(it[PhysicsComponent].body.position)
                val distance = playerPosition.minus(position).len2()
                if (distance <= aggroDistanceSqr) nearbyEntities.add(it)
            }
            behaviourTree.step()
        }
    }
}

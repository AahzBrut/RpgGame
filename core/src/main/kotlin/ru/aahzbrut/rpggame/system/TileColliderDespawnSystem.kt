package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.math.minus
import ktx.math.vec2
import ru.aahzbrut.rpggame.COLLISION_ZONE_SIZE
import ru.aahzbrut.rpggame.component.PhysicsComponent
import ru.aahzbrut.rpggame.component.TiledColliderComponent
import ru.aahzbrut.rpggame.event_bus.event.ColliderDespawnedEvent
import ru.aahzbrut.rpggame.event_bus.EventBus
import kotlin.math.absoluteValue

class TileColliderDespawnSystem(
    private val eventBus: EventBus = inject(),
) : IteratingSystem(
    family { all(TiledColliderComponent, PhysicsComponent) }
) {

    override fun onTickEntity(entity: Entity) {
        entity[TiledColliderComponent].run {
            nearbyEntities.removeIf {
                if (!it.has(PhysicsComponent)) return@removeIf true

                val entityPosition = entity[PhysicsComponent].body.position
                val nearbyPosition = it[PhysicsComponent].body.position
                val distance = vec2().set(nearbyPosition).minus(entityPosition)

                distance.x.absoluteValue.toInt() > COLLISION_ZONE_SIZE ||
                    distance.y.absoluteValue.toInt() > COLLISION_ZONE_SIZE
            }

            if (nearbyEntities.isEmpty()) {
                eventBus.fire(ColliderDespawnedEvent(cell))
                entity.remove()
            }
        }
    }
}

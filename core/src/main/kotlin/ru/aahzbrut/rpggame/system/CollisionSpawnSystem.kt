package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.loop
import ktx.math.vec2
import ktx.tiled.*
import ru.aahzbrut.rpggame.component.PhysicsComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.fromShape2D
import ru.aahzbrut.rpggame.event.MapChangedEvent

class CollisionSpawnSystem(
    private val physicsWorld: World = inject()
) : EventListener, IteratingSystem(
    family { all(PhysicsComponent) }
) {

    override fun onTickEntity(entity: Entity) = Unit

    override fun handle(event: Event?): Boolean {
        if (event is MapChangedEvent) {
            spawnColliders(event)
            spawnMapBoundaries(event)
            return true
        }

        return false
    }

    private fun spawnColliders(event: MapChangedEvent) {
        event.map.forEachLayer<TiledMapTileLayer> { layer ->
            layer.forEachCell { cell, x, y ->
                if (cell.tile.objects.isEmpty()) return@forEachCell
                cell.tile.objects.forEach { mapObject ->
                    world.entity {
                        it += fromShape2D(physicsWorld, mapObject.shape as Rectangle, x, y)
                        { _, width, height ->
                            box(width, height) { isSensor = false }
                        }
                    }
                }
            }
        }
    }

    private fun spawnMapBoundaries(event: MapChangedEvent) {
        world.entity {
            it += PhysicsComponent().apply {
                val width = event.map.width.toFloat()
                val height = event.map.height.toFloat()

                body = physicsWorld.body(BodyDef.BodyType.StaticBody) {
                    position.set(Vector2.Zero)
                    fixedRotation = true
                    allowSleep = false
                    loop(
                        vec2(0f, 0f),
                        vec2(width, 0f),
                        vec2(width, height),
                        vec2(0f, height)
                    )
                }
            }
        }
    }

    private fun TiledMapTileLayer.forEachCell(action: (TiledMapTileLayer.Cell, Int, Int) -> Unit) {
        for (x in 0 until width) {
            for (y in 0 until height) {
                getCell(x, y)?.let { action(it, x, y) }
            }
        }
    }
}

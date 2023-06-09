package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
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
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.loop
import ktx.math.vec2
import ktx.tiled.*
import ru.aahzbrut.rpggame.COLLISION_ZONE_SIZE
import ru.aahzbrut.rpggame.component.CollisionZoneComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.fromShape2D
import ru.aahzbrut.rpggame.component.TiledColliderComponent
import ru.aahzbrut.rpggame.event.ColliderDespawnedEvent
import ru.aahzbrut.rpggame.event.MapChangedEvent

class CollisionSpawnSystem(
    private val physicsWorld: World = inject()
) : EventListener, IteratingSystem(
    family { all(PhysicsComponent, CollisionZoneComponent) }
) {
    private val tileMapLayers = mutableListOf<TiledMapTileLayer>()
    private val cellsWithCollider = mutableSetOf<Cell>()

    override fun onTickEntity(entity: Entity) {
        entity[PhysicsComponent].body.position.let {bodyPosition ->
            tileMapLayers.forEach { layer ->
                layer.forEachCell(bodyPosition.x.toInt(), bodyPosition.y.toInt()) { cell, x, y ->
                    if (cell.tile.objects.isEmpty() || cellsWithCollider.contains(cell)) return@forEachCell

                    cellsWithCollider += cell

                    cell.tile.objects.forEach { mapObject ->
                        world.entity {
                            it += fromShape2D(physicsWorld, mapObject.shape as Rectangle, x, y, addFixture())

                            it += TiledColliderComponent(cell).apply {
                                nearbyEntities += entity
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addFixture(): BodyDefinition.(PhysicsComponent, Float, Float) -> Unit =
        { _, width, height ->
            box(width, height) { isSensor = false }
        }

    override fun handle(event: Event?): Boolean {
        return when (event) {
            is MapChangedEvent -> {
                tileMapLayers.clear()
                event.map.forEachLayer<TiledMapTileLayer> { tileMapLayers += it }
                spawnMapBoundaries(event)
                true
            }

            is ColliderDespawnedEvent -> {
                cellsWithCollider -= event.cell
                true
            }

            else -> false
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

    private fun TiledMapTileLayer.forEachCell(
        centerX: Int,
        centerY: Int,
        action: (Cell, Int, Int) -> Unit
    ) {
        for (x in centerX - COLLISION_ZONE_SIZE .. centerX + COLLISION_ZONE_SIZE) {
            for (y in centerY - COLLISION_ZONE_SIZE .. centerY + COLLISION_ZONE_SIZE) {
                getCell(x, y)?.let { action(it, x, y) }
            }
        }
    }
}

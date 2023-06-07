package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.inject
import ktx.app.gdxError
import ktx.math.vec2
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y
import ru.aahzbrut.rpggame.UNIT_SCALE
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.component.SpawnComponent
import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType
import ru.aahzbrut.rpggame.data.SpawnConfig
import ru.aahzbrut.rpggame.event.MapChangedEvent

class EntitySpawnSystem(
    private val atlas: TextureAtlas = inject(),
) : EventListener, IteratingSystem(
    World.family { all(SpawnComponent) }
) {
    private val spawnConfigCache = mutableMapOf<String, SpawnConfig>()
    private val sizeCache = mutableMapOf<AnimationModel, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(entity[SpawnComponent]) {
            val config = spawnConfig(type)
            world.entity {
                it += ImageComponent().apply {
                    image = Image().apply {
                        val size = config.animationModel.getSize()
                        setSize(size.x, size.y)
                        setPosition(location.x, location.y)
                    }
                }
                it += AnimationComponent().apply {
                    setNextAnimation(
                        config.animationModel,
                        AnimationType.IDLE,
                        if (config.animationModel == AnimationModel.PLAYER) FacingType.SOUTH else FacingType.NONE
                    )
                }
            }
        }
        entity.remove()
    }

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangedEvent -> {
                val entitiesLayer = event.map.layers["entities"]
                entitiesLayer.objects.forEach { mapObject ->
                    val type = mapObject.type ?: gdxError("Map object $mapObject does not have a type.")
                    world.entity {
                        it += SpawnComponent(type, vec2(mapObject.x * UNIT_SCALE, mapObject.y * UNIT_SCALE))
                    }
                }
            }
        }
        return false
    }

    private fun spawnConfig(type: String): SpawnConfig = spawnConfigCache.getOrPut(type) {
        when (type) {
            "Player" -> SpawnConfig(AnimationModel.PLAYER)
            "Slime" -> SpawnConfig(AnimationModel.SLIME)
            else -> gdxError("Unknown model type: $type")
        }
    }

    private fun AnimationModel.getSize(): Vector2 = sizeCache.getOrPut(this) {
        val regionName = when (this) {
            AnimationModel.PLAYER -> "${this.typeName}${AnimationType.IDLE.atlasKey}${FacingType.SOUTH.atlasKey}"
            else -> "${this.typeName}${AnimationType.IDLE.atlasKey}"
        }
        val regions = atlas.findRegions(regionName)
        if (regions.isEmpty) gdxError("There are no regions for idle animation for model: ${this.typeName}")
        val firstFrame = regions.first()
        Vector2(firstFrame.originalWidth * UNIT_SCALE *.5f, firstFrame.originalHeight * UNIT_SCALE *.5f)
    }
}
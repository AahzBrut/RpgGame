package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y
import ru.aahzbrut.rpggame.UNIT_SCALE
import ru.aahzbrut.rpggame.component.*
import ru.aahzbrut.rpggame.component.PhysicsComponent.Companion.fromImage
import ru.aahzbrut.rpggame.data.*
import ru.aahzbrut.rpggame.event_bus.event.EntityDamagedEvent
import ru.aahzbrut.rpggame.event_bus.event.MapChangedEvent
import ru.aahzbrut.rpggame.event_bus.EventBus

class EntitySpawnSystem(
    private val eventBus: EventBus = inject(),
    private val atlas: TextureAtlas = inject(),
    private val physicsWorld: World = inject()
) : IteratingSystem(
    family { all(SpawnComponent) }
) {
    private val spawnConfigCache = mutableMapOf<String, SpawnConfig>()
    private val sizeCache = mutableMapOf<AnimationModel, Vector2>()

    init {
        eventBus.onEvent(::handleMapChangedEvent)
    }

    override fun onTickEntity(entity: Entity) {
        with(entity[SpawnComponent]) {
            val config = spawnConfig(type)
            val newEntity = world.entity {
                it += ImageComponent().apply {
                    image = Image().apply {
                        val size = config.animationModel.getSize(config.scale)
                        setSize(size.x, size.y)
                        setPosition(location.x, location.y)
                    }
                }
                it += AnimationComponent(config.animationModel).apply {
                    setAnimation(
                        AnimationId(
                            config.animationModel,
                            AnimationType.IDLE,
                            config.defaultFacing
                        )
                    )
                }

                it += fromImage(
                    physicsWorld,
                    it[ImageComponent].image,
                    config.bodyType
                ) { _, width, height ->
                    box(width, height) {
                        isSensor = config.bodyType != StaticBody
                    }

                    if (config.bodyType != StaticBody) {
                        box(width, height * 0.4f, vec2(0f, -height * 0.12f)) {
                            isSensor = false
                        }
                    }
                }

                addCommonComponents(this, config, it)
                addOtherComponents(this, config, it)
            }
            eventBus.fire(EntityDamagedEvent(newEntity))
        }
        entity.remove()
    }

    private fun addCommonComponents(context: EntityCreateContext, config: SpawnConfig, it: Entity) {
        with(context) {
            if (config.animationModel.isAny(AnimationModel.PLAYER, AnimationModel.SLIME)) {
                it += MovementComponent(config.maxSpeed)
                it += CollisionZoneComponent()
                it += LifeComponent(10f, 10f)
                it += AttackComponent(damage = 5)
            }
        }
    }

    private fun addOtherComponents(context: EntityCreateContext, config: SpawnConfig, it: Entity) {
        with(context) {
            if (config.animationModel == AnimationModel.PLAYER) {
                it += PlayerComponent()
            }

            if (config.isStateful) it += StateComponent()
            if (config.isLootable) it += LootComponent()
            if (config.aiTreePath.isNotEmpty()) it += BehaviourTreeComponent(treePath = config.aiTreePath)
        }
    }

    private fun handleMapChangedEvent(event: MapChangedEvent) {
        val entitiesLayer = event.map.layers["entities"]
        entitiesLayer.objects.forEach { mapObject ->
            val type = mapObject.type ?: gdxError("Map object $mapObject does not have a type.")
            world.entity {
                it += SpawnComponent(type, vec2(mapObject.x * UNIT_SCALE, mapObject.y * UNIT_SCALE))
            }
        }
    }

    private fun spawnConfig(type: String): SpawnConfig = spawnConfigCache.getOrPut(type) {
        when (type) {
            "Player" -> SpawnConfig(AnimationModel.PLAYER, FacingType.SOUTH, isStateful = true, maxSpeed = 5f)
            "Slime" -> SpawnConfig(
                AnimationModel.SLIME,
                FacingType.NONE,
                isStateful = false,
                aiTreePath = "ai/slime.tree",
                maxSpeed = 1f
            )

            "Chest" -> SpawnConfig(AnimationModel.CHEST, FacingType.NONE, StaticBody, 2f, isLootable = true)
            else -> gdxError("Unknown model type: $type")
        }
    }

    private fun AnimationModel.getSize(scale: Float): Vector2 = sizeCache.getOrPut(this) {
        val regionName = when (this) {
            AnimationModel.PLAYER -> "${this.typeName}${AnimationType.IDLE.atlasKey}${FacingType.SOUTH.atlasKey}"
            else -> "${this.typeName}${AnimationType.IDLE.atlasKey}"
        }
        val regions = atlas.findRegions(regionName)
        if (regions.isEmpty) gdxError("There are no regions for idle animation for model: ${this.typeName}")
        val firstFrame = regions.first()
        Vector2(
            firstFrame.originalWidth * scale * UNIT_SCALE * 0.5f,
            firstFrame.originalHeight * scale * UNIT_SCALE * 0.5f
        )
    }
}

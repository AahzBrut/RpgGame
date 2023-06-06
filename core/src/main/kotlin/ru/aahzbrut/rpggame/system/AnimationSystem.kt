package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.app.gdxError
import ktx.collections.map
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.ImageComponent

class AnimationSystem(
    private val textureAtlas: TextureAtlas = inject(),
) : IteratingSystem(
    family { all(AnimationComponent, ImageComponent) }
) {
    companion object {
        private val logger = ktx.log.logger<AnimationSystem>()
        private const val frameDuration = 1/8f
    }

    private val animCache = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    override fun onTickEntity(entity: Entity) {
        val animComponent = entity[AnimationComponent]
        val nextAnimation = animComponent.nextAnimation

        if (nextAnimation == "") {
            animComponent.stateTime += deltaTime
        } else {
            animComponent.animation = animCache.getOrPut(nextAnimation) {
                logger.debug { "New Animation is created for $nextAnimation" }
                val regions = textureAtlas.findRegions(animComponent.nextAnimation)
                if (regions.isEmpty) gdxError("There are no texture regions for ${animComponent.nextAnimation}")
                Animation(frameDuration, regions.map { TextureRegionDrawable(it) })
            }
            animComponent.stateTime = 0f
            animComponent.nextAnimation = ""
        }

        animComponent.animation.playMode = animComponent.playMode
        entity[ImageComponent].image.drawable = animComponent.animation.getKeyFrame(animComponent.stateTime)
    }
}

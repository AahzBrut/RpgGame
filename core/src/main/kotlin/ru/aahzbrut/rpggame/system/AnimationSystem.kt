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
import ru.aahzbrut.rpggame.FRAME_DURATION
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.data.AnimationId

class AnimationSystem(
    private val textureAtlas: TextureAtlas = inject(),
) : IteratingSystem(
    family { all(AnimationComponent, ImageComponent) }
) {

    private val animCache = mutableMapOf<AnimationId, Animation<TextureRegionDrawable>>()

    override fun onTickEntity(entity: Entity) {
        val animComponent = entity[AnimationComponent]
        val nextAnimation = animComponent.nextAnimation

        if (nextAnimation == null) {
            animComponent.stateTime += deltaTime
        } else {
            animComponent.animation = animCache.getOrPut(nextAnimation) {
                val regions = textureAtlas.findRegions(nextAnimation.regionName)
                if (regions.isEmpty) gdxError("There are no texture regions for ${animComponent.nextAnimation}")
                Animation(FRAME_DURATION, regions.map { TextureRegionDrawable(it) })
            }
            animComponent.stateTime = 0f
            animComponent.nextAnimation = null
        }

        animComponent.animation.playMode = animComponent.playMode
        entity[ImageComponent].image.drawable = animComponent.animation.getKeyFrame(animComponent.stateTime)
    }
}

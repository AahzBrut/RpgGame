package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.aahzbrut.rpggame.data.AnimationId

class AnimationComponent(
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
) : Component<AnimationComponent> {
    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: AnimationId? = null
    var currentAnimation: AnimationId? = null

    override fun type() = AnimationComponent

    fun setAnimation(animationId: AnimationId){
        nextAnimation = animationId
        currentAnimation = nextAnimation
    }

    companion object : ComponentType<AnimationComponent>()
}

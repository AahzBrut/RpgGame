package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType

class AnimationComponent(
    private var atlasKey: String = "",
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
) : Component<AnimationComponent> {
    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation = ""
    var currentAnimation = ""

    override fun type() = AnimationComponent

    fun setNextAnimation(animModel: AnimationModel, type: AnimationType, facingType: FacingType){
        this.atlasKey = animModel.typeName
        nextAnimation = "$atlasKey${type.atlasKey}${facingType.atlasKey}"
        currentAnimation = nextAnimation
    }

    companion object : ComponentType<AnimationComponent>()
}

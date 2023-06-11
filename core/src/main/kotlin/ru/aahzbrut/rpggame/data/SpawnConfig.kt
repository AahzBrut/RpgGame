package ru.aahzbrut.rpggame.data

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.github.quillraven.fleks.Component

class SpawnConfig(
    val animationModel: AnimationModel,
    val defaultFacing: FacingType,
    val bodyType: BodyType = BodyType.DynamicBody,
    val scale: Float = 1f,
    val components: List<Component<*>> = emptyList()
) {
}

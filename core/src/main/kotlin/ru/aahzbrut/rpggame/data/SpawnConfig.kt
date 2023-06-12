package ru.aahzbrut.rpggame.data

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType

class SpawnConfig(
    val animationModel: AnimationModel,
    val defaultFacing: FacingType,
    val bodyType: BodyType = BodyType.DynamicBody,
    val scale: Float = 1f,
    val isStateful: Boolean = false,
    val isLootable: Boolean = false,
) {
}

package ru.aahzbrut.rpggame.data

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

enum class FacingType(
    val atlasKey: String
) {
    NONE(""),
    NORTH("-north"),
    EAST("-east"),
    SOUTH("-south"),
    WEST("-west");

    val direction: Vector2 get() = when (this) {
        NONE -> vec2(0f,0f)
        NORTH -> vec2(0f,1f)
        EAST -> vec2(1f,0f)
        SOUTH -> vec2(0f, -1f)
        WEST -> vec2(-1f, 0f)
    }
}

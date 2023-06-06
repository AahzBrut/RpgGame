package ru.aahzbrut.rpggame.data

enum class FacingType(
    val atlasKey: String
) {
    NONE(""),
    NORTH("-north"),
    EAST("-east"),
    SOUTH("-south"),
    WEST("-west");
}

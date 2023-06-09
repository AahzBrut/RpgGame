package ru.aahzbrut.rpggame.data

enum class AnimationModel(
    val typeName: String
) {
    PLAYER("player"),
    SLIME("slime"),
    CHEST("chest");

    fun isAny(vararg others: AnimationModel) : Boolean =
        others.any { it == this }
}

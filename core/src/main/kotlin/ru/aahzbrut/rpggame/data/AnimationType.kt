package ru.aahzbrut.rpggame.data

enum class AnimationType {
    IDLE,
    RUN,
    ATTACK,
    DEATH,
    OPEN;

    val atlasKey: String = "-"+toString().lowercase()
}

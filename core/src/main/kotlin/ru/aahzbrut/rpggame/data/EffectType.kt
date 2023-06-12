package ru.aahzbrut.rpggame.data

enum class EffectType {
    ATTACK,
    DEATH,
    OPEN;

    val key: String = toString().lowercase()
}

package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

class LootComponent : Component<LootComponent> {
    var lootingEntity: Entity? = null

    override fun type() = LootComponent

    companion object : ComponentType<LootComponent>()
}

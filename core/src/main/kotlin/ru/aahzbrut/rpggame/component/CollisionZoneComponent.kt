package ru.aahzbrut.rpggame.component

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

class CollisionZoneComponent : Component<CollisionZoneComponent> {
    override fun type() = CollisionZoneComponent

    companion object : ComponentType<CollisionZoneComponent>()
}

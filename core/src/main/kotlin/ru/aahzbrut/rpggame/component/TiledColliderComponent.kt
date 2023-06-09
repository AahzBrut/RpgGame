package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

class TiledColliderComponent(
    val cell: Cell,
) : Component<TiledColliderComponent> {
    val nearbyEntities = mutableSetOf<Entity>()

    override fun type() = TiledColliderComponent

    companion object : ComponentType<TiledColliderComponent>()
}

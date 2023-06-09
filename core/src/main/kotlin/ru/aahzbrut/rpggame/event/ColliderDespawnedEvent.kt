package ru.aahzbrut.rpggame.event

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import com.badlogic.gdx.scenes.scene2d.Event

class ColliderDespawnedEvent(
    val cell: Cell
) : Event()

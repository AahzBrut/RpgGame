package ru.aahzbrut.rpggame.event

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell
import ru.aahzbrut.rpggame.event_bus.GameEvent

class ColliderDespawnedEvent(
    val cell: Cell
) : GameEvent

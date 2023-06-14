package ru.aahzbrut.rpggame.event_bus.event

import com.badlogic.gdx.maps.tiled.TiledMap
import ru.aahzbrut.rpggame.event_bus.GameEvent

class MapChangedEvent(
    val map: TiledMap
) : GameEvent

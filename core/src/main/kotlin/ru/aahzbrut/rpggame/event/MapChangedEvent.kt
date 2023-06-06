package ru.aahzbrut.rpggame.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.scenes.scene2d.Event

class MapChangedEvent(
    val map: TiledMap
) : Event()

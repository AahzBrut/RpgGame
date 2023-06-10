package ru.aahzbrut.rpggame

import com.badlogic.gdx.math.Vector2

const val WINDOW_WIDTH = 16f
const val WINDOW_HEIGHT = 9f
const val UI_WINDOW_WIDTH = 1920f
const val UI_WINDOW_HEIGHT = 1080f
const val UNIT_SCALE = 1 / WINDOW_WIDTH
const val COLLISION_ZONE_SIZE: Int = 3
const val FRAME_DURATION = 1/8f

val UP = Vector2(0f, 1f)
val DOWN = Vector2(0f, -1f)
val LEFT = Vector2(-1f, 0f)
val RIGHT = Vector2(1f, 0f)

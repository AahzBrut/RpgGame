package ru.aahzbrut.rpggame.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import ru.aahzbrut.rpggame.DOWN
import ru.aahzbrut.rpggame.LEFT
import ru.aahzbrut.rpggame.RIGHT
import ru.aahzbrut.rpggame.UP

class KeyBindings(
    private val moveLeft: Array<Int> = arrayOf(Input.Keys.A),
    private val moveRight: Array<Int> = arrayOf(Input.Keys.D),
    private val moveUp: Array<Int> = arrayOf(Input.Keys.W),
    private val moveDown: Array<Int> = arrayOf(Input.Keys.S),
){

    val moveDirection: Vector2 get() {
        return vec2().also { result ->
            if (moveLeft.any { Gdx.input.isKeyPressed(it) }) result.add(LEFT)
            if (moveRight.any { Gdx.input.isKeyPressed(it) }) result.add(RIGHT)
            if (moveUp.any { Gdx.input.isKeyPressed(it) }) result.add(UP)
            if (moveDown.any { Gdx.input.isKeyPressed(it) }) result.add(DOWN)
            result.nor()
        }
    }
}

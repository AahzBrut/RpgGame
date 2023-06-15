package ru.aahzbrut.rpggame

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import kotlin.math.absoluteValue

val Fixture.entity: Entity get() = this.body.userData as Entity

fun Vector2.abs(): Vector2 = this.apply {
    x = x.absoluteValue
    y = y.absoluteValue
}

fun Vector2.reproject(from: Stage, to: Stage) {
    from.viewport.project(this)
    to.viewport.unproject(this)
}

fun addGdxInputProcessor(processor: InputProcessor) {
    val currProcessor = Gdx.input.inputProcessor
    if (currProcessor == null) {
        Gdx.input.inputProcessor = processor
    } else {
        if (currProcessor is InputMultiplexer) {
            if (processor !in currProcessor.processors) {
                currProcessor.addProcessor(processor)
            }
        } else {
            Gdx.input.inputProcessor = InputMultiplexer(currProcessor, processor)
        }
    }
}

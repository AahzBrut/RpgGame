package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType
import ktx.actors.plusAssign
import ktx.math.vec2
import ru.aahzbrut.rpggame.reproject

class FloatingTextComponent(
    val startLocation: Vector2 = vec2(),
    val targetLocation: Vector2 = vec2(),
    var lifeSpan: Float = 0f,
    var time: Float = 0f,
) : Component<FloatingTextComponent> {
    companion object : ComponentType<FloatingTextComponent>(){
        val onFloatingTextAdd: ComponentHook<FloatingTextComponent> = { _, component ->
            val uiStage = inject<Stage>("uiStage")
            val gameStage = inject<Stage>("gameStage")
            uiStage.addActor(component.label)
            component.label += Actions.fadeOut(component.lifeSpan, Interpolation.pow3OutInverse)
            @Suppress("kotlin:S6518")
            component.targetLocation.set(
                component.startLocation.x + MathUtils.random(-1f, 1f),
                component.startLocation.y + 1.5f,
            )
            component.startLocation.reproject(gameStage, uiStage)
            component.targetLocation.reproject(gameStage, uiStage)
        }

        val onFloatingTextRemove: ComponentHook<FloatingTextComponent> = { _, component ->
            val uiStage = inject<Stage>("uiStage")
            uiStage.root.removeActor(component.label)
        }
    }

    lateinit var label: Label

    override fun type() = FloatingTextComponent
}

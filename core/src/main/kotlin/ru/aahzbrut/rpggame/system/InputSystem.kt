package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.MovementComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.input.KeyBindings

class InputSystem(
    private val keyBindings: KeyBindings = inject(),
    private val uiStage: Stage = inject("uiStage")
) : IteratingSystem(
    family { all(PlayerComponent, MovementComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        entity[MovementComponent].updateDirection(keyBindings.moveDirection)
        keyBindings.toggleInventory(uiStage)
    }
}

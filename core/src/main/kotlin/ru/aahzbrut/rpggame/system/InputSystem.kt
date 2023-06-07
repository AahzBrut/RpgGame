package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ru.aahzbrut.rpggame.component.MovementComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.input.KeyBindings

class InputSystem(
    private val keyBindings: KeyBindings = inject()
) : IteratingSystem(
    family { all(PlayerComponent, MovementComponent) }
){

    override fun onTickEntity(entity: Entity) {
        val moveDirection = keyBindings.moveDirection
        entity[MovementComponent].direction.set(moveDirection)
    }
}

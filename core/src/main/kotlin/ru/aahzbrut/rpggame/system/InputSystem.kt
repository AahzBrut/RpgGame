package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import ktx.app.gdxError
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.MovementComponent
import ru.aahzbrut.rpggame.component.PlayerComponent
import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType
import ru.aahzbrut.rpggame.input.KeyBindings

class InputSystem(
    private val keyBindings: KeyBindings = inject()
) : IteratingSystem(
    family { all(PlayerComponent, MovementComponent, AnimationComponent) }
) {

    override fun onTickEntity(entity: Entity) {
        val moveDirection = keyBindings.moveDirection
        entity[MovementComponent].direction.set(moveDirection)
        val animationComponent = entity[AnimationComponent]
        val movementAnimation = when {
            moveDirection == Vector2.Zero -> "${AnimationModel.PLAYER.typeName}${AnimationType.IDLE.atlasKey}${FacingType.SOUTH.atlasKey}"
            moveDirection.x > 0f -> "${AnimationModel.PLAYER.typeName}${AnimationType.RUN.atlasKey}${FacingType.EAST.atlasKey}"
            moveDirection.x < 0f -> "${AnimationModel.PLAYER.typeName}${AnimationType.RUN.atlasKey}${FacingType.WEST.atlasKey}"
            moveDirection.y > 0f -> "${AnimationModel.PLAYER.typeName}${AnimationType.RUN.atlasKey}${FacingType.NORTH.atlasKey}"
            moveDirection.y < 0f -> "${AnimationModel.PLAYER.typeName}${AnimationType.RUN.atlasKey}${FacingType.SOUTH.atlasKey}"
            else -> gdxError("Impossible animation for movement direction.")
        }
        if (animationComponent.currentAnimation != movementAnimation) {
            when {
                moveDirection == Vector2.Zero -> animationComponent.setNextAnimation(AnimationModel.PLAYER, AnimationType.IDLE, FacingType.SOUTH)
                moveDirection.x > 0f -> animationComponent.setNextAnimation(AnimationModel.PLAYER, AnimationType.RUN, FacingType.EAST)
                moveDirection.x < 0f -> animationComponent.setNextAnimation(AnimationModel.PLAYER, AnimationType.RUN, FacingType.WEST)
                moveDirection.y > 0f -> animationComponent.setNextAnimation(AnimationModel.PLAYER,AnimationType.RUN, FacingType.NORTH)
                moveDirection.y < 0f -> animationComponent.setNextAnimation(AnimationModel.PLAYER, AnimationType.RUN, FacingType.SOUTH)
                else -> gdxError("Impossible animation for movement direction.")
            }
        }

    }
}

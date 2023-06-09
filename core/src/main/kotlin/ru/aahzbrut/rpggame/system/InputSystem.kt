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
import ru.aahzbrut.rpggame.data.AnimationId
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
            moveDirection == Vector2.Zero -> keepDirectionIdle(animationComponent.currentAnimation)
            moveDirection.x > 0f -> AnimationId(AnimationModel.PLAYER, AnimationType.RUN, FacingType.EAST)
            moveDirection.x < 0f -> AnimationId(AnimationModel.PLAYER, AnimationType.RUN, FacingType.WEST)
            moveDirection.y > 0f -> AnimationId(AnimationModel.PLAYER, AnimationType.RUN, FacingType.NORTH)
            moveDirection.y < 0f -> AnimationId(AnimationModel.PLAYER, AnimationType.RUN, FacingType.SOUTH)
            else -> gdxError("Impossible animation for movement direction.")
        }
        if (animationComponent.currentAnimation != movementAnimation) {
            animationComponent.setAnimation(movementAnimation)
        }
    }

    private fun keepDirectionIdle(animationId: AnimationId?): AnimationId {
        return animationId?.let {
            AnimationId(AnimationModel.PLAYER, AnimationType.IDLE, it.facing)
        } ?: AnimationId(AnimationModel.PLAYER, AnimationType.IDLE, FacingType.SOUTH)
    }
}

package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.app.gdxError
import ktx.math.vec2
import ru.aahzbrut.rpggame.component.*
import ru.aahzbrut.rpggame.data.AnimationId
import ru.aahzbrut.rpggame.data.AnimationModel
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType
import ru.aahzbrut.rpggame.input.KeyBindings

class StateContext(
    private val entity: Entity,
    private val world: World,
) {

    val isAnimationDone: Boolean
        get() = with(world) {
            return@with entity.getOrNull(AnimationComponent)?.isDone ?: true
        }

    val isDead: Boolean get() = with(world) { return@with entity has DeathComponent }

    val wantsToRun: Boolean
        get() {
            with(world) {
                return entity[MovementComponent].direction != Vector2.Zero
            }
        }
    val wantsToAttack: Boolean
        get() {
            with(world) {
                return inject<KeyBindings>().isTryingToAttack
            }
        }

    fun animation(animationType: AnimationType, playMode: PlayMode = PlayMode.LOOP, reset: Boolean = false) {
        with(world) {
            entity[AnimationComponent].run {
                setAnimation(animationType)
                this.playMode = playMode
                if (reset) stateTime = 0f
            }
        }
    }

    fun animation(
        animationType: AnimationType,
        facing: FacingType,
        playMode: PlayMode = PlayMode.LOOP,
        reset: Boolean = false
    ) {
        with(world) {
            entity[AnimationComponent].run {
                setAnimation(animationType, facing)
                this.playMode = playMode
                if (reset) stateTime = 0f
            }
        }
    }

    fun state(newState: AiState, immediate: Boolean = false) {
        with(world) {
            entity[StateComponent].run {
                nextState = newState
                if (immediate) stateMachine.changeState(newState)
            }
        }
    }

    fun startAttack() {
        with(world) {
            entity.getOrNull(AttackComponent)?.startAttack()
        }
    }

    fun stopRunning() {
        with(world) {
            entity.getOrNull(MovementComponent)?.run {
                direction.setZero()
            }
        }
    }

    fun attackComponent(): AttackComponent? {
        with(world) {
            return entity.getOrNull(AttackComponent)
        }
    }

    fun setPreviousState() {
        with(world) {
            entity[StateComponent].run {
                nextState = stateMachine.previousState
            }
        }
    }

    fun enableGlobalState(value: Boolean) {
        with(world) {
            entity[StateComponent].run {
                if (value) {
                    stateMachine.globalState = GlobalState.CHECK_ALIVE
                } else {
                    stateMachine.globalState = null
                }
            }
        }
    }

    fun updateFacing(facing: FacingType) {
        with(world) {
            entity[AnimationComponent].run {
                this.updateFacing(facing)
            }
        }
    }

    fun enableMovement(value: Boolean) {
        with(world) {
            entity.getOrNull(MovementComponent)?.run {
                enabled = value
            }
        }
    }

    fun updateRunAnimation() {
        with(world) {
            val moveDirection = vec2().set(entity[MovementComponent].direction)
            val animationComponent = entity[AnimationComponent]
            val movementAnimation = when {
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
    }
}

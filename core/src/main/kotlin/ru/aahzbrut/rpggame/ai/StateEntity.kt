package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ru.aahzbrut.rpggame.component.AnimationComponent
import ru.aahzbrut.rpggame.component.AttackComponent
import ru.aahzbrut.rpggame.component.MovementComponent
import ru.aahzbrut.rpggame.component.StateComponent
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.input.KeyBindings

class StateEntity(
    private val entity: Entity,
    private val world: World,
) {
    val wantsToRun: Boolean
        get() {
            with(world) {
                return entity.getOrNull(MovementComponent)?.direction != Vector2.Zero
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

    fun state(newState: AiState) {
        with(world) {
            entity[StateComponent].nextState = newState
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
}

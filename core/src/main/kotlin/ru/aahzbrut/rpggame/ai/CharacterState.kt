package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.graphics.g2d.Animation
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType

enum class CharacterState : AiState {
    IDLE {
        override fun enter(context: StateContext) {
            context.animation(AnimationType.IDLE)
        }

        override fun update(context: StateContext) {
            when {
                context.wantsToAttack -> context.state(ATTACK, true)
                context.wantsToRun -> context.state(RUN, true)
            }
        }
    },
    RUN {
        override fun enter(context: StateContext) {
            if (context.wantsToRun) context.updateRunAnimation()
        }

        override fun update(context: StateContext) {
            when {
                context.wantsToAttack -> {
                    context.state(ATTACK, true)
                    return
                }
                !context.wantsToRun -> {
                    context.state(IDLE, true)
                    return
                }
            }
            context.updateRunAnimation()
        }

        override fun exit(context: StateContext) {
            context.stopRunning()
        }
    },
    ATTACK {
        override fun enter(context: StateContext) {
            context.enableMovement(false)
            context.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL)
            context.startAttack()
        }

        override fun update(context: StateContext) {
            context.attackComponent()?.run {
                if (isReady && !startAttack) {
                    context.enableMovement(true)
                    context.setPreviousState()
                } else if (isReady) {
                    context.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL, true)
                    context.startAttack()
                }
            }
        }
    },
    DEAD {
        override fun enter(context: StateContext) {
            context.animation(AnimationType.DEATH, FacingType.NONE, Animation.PlayMode.NORMAL)
            context.enableMovement(false)
        }

        override fun update(context: StateContext) {
            if (!context.isDead) {
                context.state(RESURRECT)
            }
        }
    },
    RESURRECT {
        override fun enter(context: StateContext) {
            context.enableGlobalState(true)
            context.animation(AnimationType.DEATH, FacingType.NONE, Animation.PlayMode.REVERSED, true)
        }

        override fun update(context: StateContext) {
            if (context.isAnimationDone) {
                context.updateFacing(FacingType.SOUTH)
                context.state(IDLE)
            }
        }
    }
}

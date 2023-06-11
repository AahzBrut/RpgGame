package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.graphics.g2d.Animation
import ru.aahzbrut.rpggame.data.AnimationType

enum class CharacterState : AiState {
    IDLE {
        override fun enter(context: StateContext) {
            context.animation(AnimationType.IDLE)
        }

        override fun update(context: StateContext) {
            when {
                context.wantsToAttack -> context.state(ATTACK)
                context.wantsToRun -> context.state(RUN)
            }
        }
    },
    RUN {
        override fun enter(context: StateContext) {
            context.animation(AnimationType.RUN)
        }

        override fun update(context: StateContext) {
            when {
                context.wantsToAttack -> context.state(ATTACK)
                !context.wantsToRun -> context.state(IDLE)
            }
        }

        override fun exit(context: StateContext) {
            context.stopRunning()
        }
    },
    ATTACK {
        override fun enter(context: StateContext) {
            context.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL)
            context.startAttack()
        }

        override fun update(context: StateContext) {
            context.attackComponent()?.run {
                if (isReady && !startAttack) {
                    context.setPreviousState()
                } else if (isReady) {
                    context.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL, true)
                    context.startAttack()
                }
            }
        }
    },
    DEAD,
    RESURRECT
}

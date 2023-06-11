package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.graphics.g2d.Animation
import ru.aahzbrut.rpggame.data.AnimationType

enum class DefaultState : AiState {
    IDLE {
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.IDLE)
        }

        override fun update(entity: StateEntity) {
            when {
                entity.wantsToAttack -> entity.state(ATTACK)
                entity.wantsToRun -> entity.state(RUN)
            }
        }
    },
    RUN {
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.RUN)
        }

        override fun update(entity: StateEntity) {
            when {
                entity.wantsToAttack -> entity.state(ATTACK)
                !entity.wantsToRun -> entity.state(IDLE)
            }
        }

        override fun exit(entity: StateEntity) {
            entity.stopRunning()
        }
    },
    ATTACK {
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL)
            entity.startAttack()
        }

        override fun update(entity: StateEntity) {
            entity.attackComponent()?.run {
                if (isReady && !startAttack) {
                    entity.setPreviousState()
                } else if (isReady) {
                    entity.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL, true)
                    entity.startAttack()
                }
            }
        }
    },
    DEAD,
    RESURRECT
}

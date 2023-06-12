package ru.aahzbrut.rpggame.ai.character_state

import com.badlogic.gdx.graphics.g2d.Animation
import ru.aahzbrut.rpggame.ai.StateContext
import ru.aahzbrut.rpggame.data.AnimationType

object ATTACK : CharacterState() {
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
}

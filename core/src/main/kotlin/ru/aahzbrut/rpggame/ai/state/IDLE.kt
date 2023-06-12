package ru.aahzbrut.rpggame.ai.state

import ru.aahzbrut.rpggame.ai.StateContext
import ru.aahzbrut.rpggame.data.AnimationType

object IDLE : CharacterState() {
    override fun enter(context: StateContext) {
        context.animation(AnimationType.IDLE)
    }

    override fun update(context: StateContext) {
        when {
            context.wantsToAttack -> context.state(ATTACK, true)
            context.wantsToRun -> context.state(RUN, true)
        }
    }
}

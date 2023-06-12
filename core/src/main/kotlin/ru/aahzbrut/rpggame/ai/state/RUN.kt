package ru.aahzbrut.rpggame.ai.state

import ru.aahzbrut.rpggame.ai.StateContext

object RUN : CharacterState() {
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
}

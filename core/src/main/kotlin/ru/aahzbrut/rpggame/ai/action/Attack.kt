package ru.aahzbrut.rpggame.ai.action

import com.badlogic.gdx.graphics.g2d.Animation
import ru.aahzbrut.rpggame.ai.Action
import ru.aahzbrut.rpggame.data.AnimationType

class Attack : Action(){
    override fun execute(): Status {
        if (status != Status.RUNNING) {
            context.animation(AnimationType.ATTACK, Animation.PlayMode.NORMAL, true)
            context.startAttack()
            return Status.RUNNING
        }

        return if (context.isAnimationDone) {
            context.animation(AnimationType.IDLE)
            context.stopRunning()
            Status.SUCCEEDED
        } else Status.RUNNING
    }
}

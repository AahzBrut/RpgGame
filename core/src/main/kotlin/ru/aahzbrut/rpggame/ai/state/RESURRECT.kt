package ru.aahzbrut.rpggame.ai.state

import com.badlogic.gdx.graphics.g2d.Animation
import ru.aahzbrut.rpggame.ai.StateContext
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType

object RESURRECT : CharacterState() {
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

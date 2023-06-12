package ru.aahzbrut.rpggame.ai.character_state

import com.badlogic.gdx.graphics.g2d.Animation
import ru.aahzbrut.rpggame.ai.StateContext
import ru.aahzbrut.rpggame.data.AnimationType
import ru.aahzbrut.rpggame.data.FacingType

object DEAD : CharacterState() {
    override fun enter(context: StateContext) {
        context.animation(AnimationType.DEATH, FacingType.NONE, Animation.PlayMode.NORMAL)
        context.enableMovement(false)
    }

    override fun update(context: StateContext) {
        if (!context.isDead) {
            context.state(RESURRECT)
        }
    }
}

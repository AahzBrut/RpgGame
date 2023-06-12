package ru.aahzbrut.rpggame.ai.action

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.math.MathUtils
import ktx.math.vec2
import ru.aahzbrut.rpggame.ai.Action
import ru.aahzbrut.rpggame.data.AnimationType

class Wander : Action(){
    private val startPosition= vec2()
    private val targetPosition = vec2()
    private var currentDuration = 0f

    override fun execute(): Status {
        if (status != Status.RUNNING) {
            context.animation(AnimationType.RUN)
            if (startPosition.isZero) startPosition.set(context.position)
            targetPosition.set(startPosition)
            targetPosition.x += MathUtils.random(-2f,2f)
            targetPosition.y += MathUtils.random(-2f,2f)
            context.moveTo(targetPosition)
            currentDuration = 1f
            return Status.RUNNING
        }

        if (context.inRange(0.1f, targetPosition)) {
            context.moveTo(targetPosition)
            context.stopRunning()
            return Status.SUCCEEDED
        }

        currentDuration -= GdxAI.getTimepiece().deltaTime
        if (currentDuration <= 0f) return Status.SUCCEEDED

        return Status.RUNNING
    }
}

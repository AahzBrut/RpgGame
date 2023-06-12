package ru.aahzbrut.rpggame.ai.action

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.ai.utils.random.FloatDistribution
import ru.aahzbrut.rpggame.ai.Action
import ru.aahzbrut.rpggame.ai.StateContext
import ru.aahzbrut.rpggame.data.AnimationType

class Idle(
    @JvmField
    @TaskAttribute(required = true)
    var duration: FloatDistribution? = null
) : Action() {
    private var currentDuration = 0f

    override fun execute(): Status {
        if (status != Status.RUNNING) {
            context.animation(AnimationType.IDLE)
            currentDuration = duration?.nextFloat() ?: 1f
            return Status.RUNNING
        }

        currentDuration -= GdxAI.getTimepiece().deltaTime

        if (currentDuration <= 0f) return Status.SUCCEEDED
        return Status.RUNNING
    }

    @Suppress("kotlin:S6530")
    override fun copyTo(task: Task<StateContext>): Task<StateContext> {
        return task.also {
            (it as Idle).duration = duration
        }
    }
}

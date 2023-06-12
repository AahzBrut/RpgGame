package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task

abstract class Condition : LeafTask<StateContext>(){
    val context: StateContext get() = `object`

    abstract fun condition() : Boolean

    override fun execute(): Status {
        return if (condition()) Status.SUCCEEDED else Status.FAILED
    }

    override fun copyTo(task: Task<StateContext>): Task<StateContext> = task
}

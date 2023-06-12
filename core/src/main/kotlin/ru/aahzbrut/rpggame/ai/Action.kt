package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task

abstract class Action : LeafTask<StateContext>() {
    val context: StateContext get() = `object`

    override fun copyTo(task: Task<StateContext>): Task<StateContext> = task
}

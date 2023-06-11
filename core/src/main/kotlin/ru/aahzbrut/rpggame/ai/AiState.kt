package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

interface AiState : State<StateContext> {
    override fun enter(context: StateContext) = Unit

    override fun update(context: StateContext) = Unit

    override fun exit(context: StateContext) = Unit

    override fun onMessage(context: StateContext, telegram: Telegram?): Boolean = false
}

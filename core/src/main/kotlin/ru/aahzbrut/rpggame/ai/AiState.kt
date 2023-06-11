package ru.aahzbrut.rpggame.ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

interface AiState : State<StateEntity> {
    override fun enter(entity: StateEntity) = Unit

    override fun update(entity: StateEntity) = Unit

    override fun exit(entity: StateEntity) = Unit

    override fun onMessage(entity: StateEntity, telegram: Telegram?): Boolean = false
}

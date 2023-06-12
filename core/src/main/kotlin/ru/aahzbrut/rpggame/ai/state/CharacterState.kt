package ru.aahzbrut.rpggame.ai.state

import ru.aahzbrut.rpggame.ai.AiState

sealed class CharacterState : AiState {
    companion object {
        fun values(): Array<CharacterState> {
            return arrayOf(IDLE, RUN, ATTACK, DEAD, RESURRECT)
        }
    }
}

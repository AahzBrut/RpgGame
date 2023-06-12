package ru.aahzbrut.rpggame.ai.character_state

import ru.aahzbrut.rpggame.ai.AiState

sealed class CharacterState : AiState {
    companion object {
        fun values(): Array<CharacterState> {
            return arrayOf(IDLE, RUN, ATTACK, DEAD, RESURRECT)
        }

        fun valueOf(value: String): CharacterState {
            return when (value) {
                "IDLE" -> IDLE
                "RUN" -> RUN
                "ATTACK" -> ATTACK
                "DEAD" -> DEAD
                "RESURRECT" -> RESURRECT
                else -> throw IllegalArgumentException("No object ru.aahzbrut.rpggame.ai.character_state.CharacterState.$value")
            }
        }
    }
}

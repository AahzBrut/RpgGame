package ru.aahzbrut.rpggame.ai

import ru.aahzbrut.rpggame.ai.state.DEAD

enum class GlobalState: AiState {
    CHECK_ALIVE {
        override fun update(context: StateContext) {
            if (context.isDead) {
                context.enableGlobalState(false)
                context.state(DEAD, true)
            }
        }
    },
}

package ru.aahzbrut.rpggame.ai

enum class GlobalState: AiState {
    CHECK_ALIVE {
        override fun update(context: StateContext) {
            if (context.isDead) {
                context.enableGlobalState(false)
                context.state(CharacterState.DEAD, true)
            }
        }
    },
}

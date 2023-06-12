package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType
import ru.aahzbrut.rpggame.ai.StateContext
import ru.aahzbrut.rpggame.ai.AiState
import ru.aahzbrut.rpggame.ai.GlobalState
import ru.aahzbrut.rpggame.ai.character_state.IDLE

class StateComponent(
    val stateMachine: DefaultStateMachine<StateContext, AiState> = DefaultStateMachine(),
    var nextState: AiState = IDLE,
) : Component<StateComponent> {

    init {
        stateMachine.globalState = GlobalState.CHECK_ALIVE
    }

    companion object : ComponentType<StateComponent>(){
        val onStateAdd: ComponentHook<StateComponent> = { entity, component ->
            component.stateMachine.owner = StateContext(entity, this)
        }
    }

    override fun type() = StateComponent
}

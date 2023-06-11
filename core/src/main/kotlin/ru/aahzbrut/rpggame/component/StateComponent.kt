package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType
import ru.aahzbrut.rpggame.ai.StateEntity
import ru.aahzbrut.rpggame.ai.DefaultState
import ru.aahzbrut.rpggame.ai.AiState

class StateComponent(
    val stateMachine: DefaultStateMachine<StateEntity, AiState> = DefaultStateMachine(),
    var nextState: AiState = DefaultState.IDLE,
) : Component<StateComponent> {

    companion object : ComponentType<StateComponent>(){
        val onStateAdd: ComponentHook<StateComponent> = { entity, component ->
            component.stateMachine.owner = StateEntity(entity, this)
        }
    }

    override fun type() = StateComponent
}

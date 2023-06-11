package ru.aahzbrut.rpggame.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.aahzbrut.rpggame.component.StateComponent

class StateSystem: IteratingSystem(
    family { all(StateComponent) }
) {
    override fun onTickEntity(entity: Entity) {
        with(entity[StateComponent]){
            if (nextState != stateMachine.currentState) stateMachine.changeState(nextState)
            stateMachine.update()
        }
    }
}

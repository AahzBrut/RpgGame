package ru.aahzbrut.rpggame.ai.condition

import ru.aahzbrut.rpggame.ai.Condition

class CanAttack : Condition(){
    override fun condition(): Boolean = context.canAttack
}

package ru.aahzbrut.rpggame.ai.condition

import ru.aahzbrut.rpggame.ai.Condition

class IsEnemyNearby : Condition(){
    override fun condition(): Boolean = context.isEnemyNearby
}

package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2
import ru.aahzbrut.rpggame.data.AttackState


class AttackComponent(
    var startAttack: Boolean = false,
    var state: AttackState = AttackState.READY,
    var damage: Int = 0,
    var delay: Float = 0f,
    var maxDelay: Float = 1.5f,
    var extraRange: Float = 0f,
    var attackAreaCenter: Vector2 = vec2(),
    var attackAreaSize: Vector2 = vec2(),
) : Component<AttackComponent> {
    companion object : ComponentType<AttackComponent>()

    val isReady: Boolean get() = state == AttackState.READY
    val isPrepared: Boolean get() = state == AttackState.PREPARE
    val isAttacking: Boolean get() = state == AttackState.ATTACK

    override fun type() = AttackComponent

    fun startAttack() {
        startAttack = true
        state = AttackState.PREPARE
    }
}

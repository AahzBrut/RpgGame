package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.assets.disposeSafely
import ktx.graphics.rect
import ktx.graphics.use
import ktx.math.minus
import ktx.math.vec2
import ru.aahzbrut.rpggame.component.AttackComponent
import ru.aahzbrut.rpggame.component.PhysicsComponent

class DebugSystem(
    private val physicsWorld: World = inject(),
    private val stage: Stage = inject()
) : IntervalSystem(enabled = true) {
    private lateinit var physicsRenderer: Box2DDebugRenderer
    private lateinit var shapeRenderer: ShapeRenderer

    init {
        if (enabled) {
            physicsRenderer = Box2DDebugRenderer()
            shapeRenderer = ShapeRenderer()
        }
    }

    override fun onTick() {
        physicsRenderer.render(physicsWorld, stage.camera.combined)
        shapeRenderer.use(ShapeRenderer.ShapeType.Line, stage.camera){ renderer->
            renderer.setColor(1f, 0f, 0f, 1f)
            world.family { all(AttackComponent, PhysicsComponent) }.forEach {
                with(it[AttackComponent]){
                    val halfSize = vec2().set(it[PhysicsComponent].size).scl(0.5f)
                    if (this.attackAreaCenter != Vector2.Zero){
                        val screenCenter = vec2().set(attackAreaCenter).minus(halfSize)
                        renderer.rect(screenCenter, attackAreaSize)
                    }
                }
            }
        }
    }

    override fun onDispose() {
        if (enabled) {
            physicsRenderer.disposeSafely()
            shapeRenderer.disposeSafely()
        }
    }
}

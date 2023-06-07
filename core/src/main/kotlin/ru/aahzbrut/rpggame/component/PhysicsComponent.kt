package ru.aahzbrut.rpggame.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentHook
import com.github.quillraven.fleks.ComponentType
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.math.vec2

class PhysicsComponent(
    val impulse: Vector2 = vec2()
) : Component<PhysicsComponent> {
    lateinit var body: Body
    val prevPos = vec2()

    override fun type() = PhysicsComponent

    companion object : ComponentType<PhysicsComponent>() {
        val onPhysicAdd: ComponentHook<PhysicsComponent> = { entity, component ->
            component.body.userData = entity
        }

        val onPhysicRemove: ComponentHook<PhysicsComponent> = { _, component ->
            val body = component.body
            body.world.destroyBody(body)
            body.userData = null
        }

        fun fromImage(
            world: World,
            image: Image,
            bodyType: BodyDef.BodyType,
            fixtureAction: BodyDefinition.(PhysicsComponent, Float, Float) -> Unit
        ): PhysicsComponent {
            return PhysicsComponent().apply {
                body = world.body(bodyType) {
                    image.let {
                        @Suppress("kotlin:S6518")
                        position.set(it.x + it.width * 0.5f, it.y + it.height * 0.5f)
                        fixedRotation = true
                        allowSleep = false
                        this.fixtureAction(this@apply, it.width, it.height)
                    }
                }
                prevPos.set(body.position)
            }
        }

    }
}

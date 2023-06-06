package ru.aahzbrut.rpggame.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ru.aahzbrut.rpggame.component.ImageComponent
import ru.aahzbrut.rpggame.system.RenderSystem

class GameScreen : KtxScreen {
    companion object {
        val logger = ktx.log.logger<GameScreen>()
    }

    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private val playerTexture: Texture = Texture("assets/graphics/characters/player.png")
    private val slimeTexture: Texture = Texture("assets/graphics/characters/slime.png")

    private val world = world {
        injectables {
            add(stage)
        }

        components {
            onAdd(ImageComponent){_, imageComponent -> stage.addActor(imageComponent.image)}
            onRemove(ImageComponent){_, imageComponent ->  stage.root.removeActor(imageComponent.image)}
        }

        systems {
            add(RenderSystem())
        }
    }

    override fun show() {
        logger.debug { "GameScreen displayed." }

        world.entity{
            it += ImageComponent().apply {
                image = Image(TextureRegion( playerTexture, 48, 48)).apply {
                    setSize(4f,4f)
                }
            }
        }

        world.entity{
            it += ImageComponent().apply {
                image = Image(TextureRegion( slimeTexture, 32, 32)).apply {
                    setSize(4f,4f)
                    setPosition(4f, 0f)
                }
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        stage.disposeSafely()
        playerTexture.disposeSafely()
        slimeTexture.disposeSafely()
        world.dispose()
    }
}

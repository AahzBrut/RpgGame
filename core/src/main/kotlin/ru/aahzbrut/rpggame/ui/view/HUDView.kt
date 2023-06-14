package ru.aahzbrut.rpggame.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.scene2d.KTable
import ktx.scene2d.label
import ktx.scene2d.table
import ru.aahzbrut.rpggame.ui.resource.Drawables
import ru.aahzbrut.rpggame.ui.resource.Labels
import ru.aahzbrut.rpggame.ui.get
import ru.aahzbrut.rpggame.ui.characterInfo
import ru.aahzbrut.rpggame.ui.model.GameModel
import ru.aahzbrut.rpggame.ui.resetFadeOutDelay
import ru.aahzbrut.rpggame.ui.widget.CharacterInfo

class HUDView(
    model: GameModel,
    skin: Skin,
) : Table(skin), KTable {
    private val playerInfo: CharacterInfo
    private val enemyInfo: CharacterInfo
    private val popupLabel: Label

    init {
        setFillParent(true)

        enemyInfo = characterInfo(Drawables.SLIME) {
            alpha = 0f
            it.row()
        }

        table {
            alpha = 0f
            background = skin[Drawables.FRAME_BACKGROUND]
            this@HUDView.popupLabel = label("", style = Labels.FRAME.skinKey) { cell ->
                setAlignment(Align.topLeft)
                wrap = true
                cell.expand().fill().pad(14f)
            }
            it.expand().width(130f).height(90f).center().row()
        }

        playerInfo = characterInfo(Drawables.PLAYER) {
            it.align(Align.left)
        }

        model.onPropertyChange(GameModel::playerLifeAmount) {lifeAmount->
            setPlayerLifeValue(lifeAmount)
        }

        model.onPropertyChange(GameModel::enemyLifeAmount) {lifeAmount->
            showEnemyInfo(Drawables.SLIME, lifeAmount)
            setEnemyLifeValue(lifeAmount)
        }

        model.onPropertyChange(GameModel::lootText) {message->
            popupMessage(message)
        }
    }

    private fun setPlayerLifeValue(percentage: Float) = playerInfo.setLifeValue(percentage)

    private fun setEnemyLifeValue(percentage: Float) = enemyInfo.setLifeValue(percentage)

    @Suppress("SameParameterValue")
    private fun showEnemyInfo(portrait: Drawables?, percentage: Float) {
        enemyInfo.setCharacterPortrait(portrait)
        enemyInfo.setLifeValue(percentage, 0f)

        if (enemyInfo.alpha == 0f) {
            enemyInfo.clearActions()
            enemyInfo += sequence(fadeIn(1f, Interpolation.linear), delay(5f, fadeOut(.5f)))
        } else {
            enemyInfo.resetFadeOutDelay()
        }
    }

    private fun popupMessage(text: String) {
        popupLabel.txt = text

        if (popupLabel.parent.alpha == 0f) {
            popupLabel.parent.clearActions()
            popupLabel.parent += sequence(fadeIn(1f, Interpolation.smooth), delay(5f, fadeOut(1f)))
        } else {
            popupLabel.parent.resetFadeOutDelay()
        }
    }
}

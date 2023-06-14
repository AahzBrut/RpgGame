package ru.aahzbrut.rpggame.ui.widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import ktx.actors.plusAssign
import ktx.scene2d.KGroup
import ru.aahzbrut.rpggame.ui.resource.Drawables
import ru.aahzbrut.rpggame.ui.get

class CharacterInfo(
    private val skin: Skin,
    charDrawable: Drawables?,
) : WidgetGroup(), KGroup {
    private val background: Image = Image(skin[Drawables.CHAR_INFO_BACKGROUND])
    private val characterBackground: Image = Image(charDrawable?.let { skin[it] })
    private val lifeBar: Image = Image(skin[Drawables.LIFE_BAR])
    private val manaBar: Image = Image(skin[Drawables.MANA_BAR])

    init {
        this += background
        this += characterBackground.apply {
            setPosition(2f, 2f)
            setSize(22f, 20f)
            setScaling(Scaling.contain)
        }
        this += lifeBar.apply { setPosition(26f, 19f) }
        this += manaBar.apply { setPosition(26f, 13f) }
    }

    override fun getPrefWidth() = background.drawable.minWidth

    override fun getPrefHeight() = background.drawable.minHeight

    fun setCharacterPortrait(charDrawable: Drawables?) {
        characterBackground.drawable = charDrawable?.let { skin[it] }
    }

    fun setLifeValue(percentage: Float, duration: Float = 0.75f) {
        lifeBar.clearActions()
        lifeBar += Actions.scaleTo(MathUtils.clamp(percentage, 0f, 1f), 1f, duration)
    }
}


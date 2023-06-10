package ru.aahzbrut.rpggame.data

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Disposable

class UIStyles(
    private val floatingLabelFont: BitmapFont = BitmapFont(Gdx.files.internal("damage.fnt")),
    val floatingLabelStyle: LabelStyle = LabelStyle(floatingLabelFont, Color.RED)
) : Disposable {

    override fun dispose() {
        floatingLabelFont.dispose()
    }
}

package ru.aahzbrut.rpggame.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.set
import ktx.style.skin
import ru.aahzbrut.rpggame.ui.resource.Drawables
import ru.aahzbrut.rpggame.ui.resource.Fonts
import ru.aahzbrut.rpggame.ui.resource.Labels

operator fun Skin.get(drawable: Drawables): Drawable = this.getDrawable(drawable.regionName)
operator fun Skin.get(font: Fonts): BitmapFont = this.getFont(font.skinKey)

fun loadSkin(){
    Scene2DSkin.defaultSkin = skin(TextureAtlas("ui/ui.atlas")){skin->
        Fonts.values().forEach {font ->
            skin[font.skinKey] = BitmapFont(Gdx.files.internal(font.fontPath), skin.getRegion(font.regionName)).apply {
                data.setScale(font.scale)
                data.markupEnabled = true
            }
        }

        label(Labels.FRAME.skinKey) {
            font = skin[Fonts.DEFAULT]
            background = skin[Drawables.FRAME_FOREGROUND].apply {
                leftWidth = 3f
                rightWidth = 3f
                topHeight = 1f
            }
        }

        label(Labels.TITLE.skinKey) {
            font = skin[Fonts.CAPTION]
            fontColor = Color.SLATE
            background = skin[Drawables.FRAME_FOREGROUND].apply {
                leftWidth = 3f
                rightWidth = 3f
                topHeight = 1f
            }
        }
    }
}

fun disposeSkin() {
    Scene2DSkin.defaultSkin.disposeSafely()
}

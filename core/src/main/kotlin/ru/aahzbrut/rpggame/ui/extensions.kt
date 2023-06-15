package ru.aahzbrut.rpggame.ui

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ru.aahzbrut.rpggame.ui.model.GameModel
import ru.aahzbrut.rpggame.ui.model.InventoryModel
import ru.aahzbrut.rpggame.ui.resource.Drawables
import ru.aahzbrut.rpggame.ui.view.HUDView
import ru.aahzbrut.rpggame.ui.view.InventoryView
import ru.aahzbrut.rpggame.ui.widget.CharacterInfo
import ru.aahzbrut.rpggame.ui.widget.InventorySlot

@Scene2dDsl
fun <S> KWidget<S>.hudView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: HUDView.(S) -> Unit = {}
): HUDView = actor( HUDView(model, skin), init)

@Scene2dDsl
fun <S> KWidget<S>.characterInfo(
    charDrawable: Drawables?,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: CharacterInfo.(S) -> Unit = {}
): CharacterInfo = actor( CharacterInfo(skin, charDrawable), init)

@Scene2dDsl
fun <S> KWidget<S>.inventoryView(
    model: InventoryModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventoryView.(S) -> Unit = {}
): InventoryView = actor( InventoryView(model, skin), init)

@Scene2dDsl
fun <S> KWidget<S>.inventorySlot(
    slotBackground: Drawables? = null,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: InventorySlot.(S) -> Unit = {}
): InventorySlot = actor( InventorySlot(slotBackground, skin), init)


fun Actor.resetFadeOutDelay() {
    actions
        .filterIsInstance<SequenceAction>()
        .lastOrNull()?.let {
            val delay = it.actions.last() as DelayAction
            delay.time = 0f
        }
}

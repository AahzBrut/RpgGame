package ru.aahzbrut.rpggame.ui.widget

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.scene2d.KGroup
import ru.aahzbrut.rpggame.data.ItemCategory
import ru.aahzbrut.rpggame.data.ItemCategory.*
import ru.aahzbrut.rpggame.ui.get
import ru.aahzbrut.rpggame.ui.model.ItemModel
import ru.aahzbrut.rpggame.ui.resource.Drawables
import ru.aahzbrut.rpggame.ui.resource.Drawables.*

class InventorySlot(
    private val slotItemBackground: Drawables?,
    private val skin: Skin,
) : WidgetGroup(), KGroup {
    private val backgroundImage = Image(skin[INVENTORY_SLOT])
    private val slotItemBackgroundImage: Image? = slotItemBackground?.let { Image(skin[slotItemBackground]) }
    val itemImage: Image = Image()
    var itemModel: ItemModel? = null
    val supportedCategory: ItemCategory get() = when (slotItemBackground){
        INVENTORY_SLOT_HELMET -> HELMET
        INVENTORY_SLOT_ARMOR -> ARMOR
        INVENTORY_SLOT_BOOTS -> BOOTS
        INVENTORY_SLOT_WEAPON -> WEAPON
        else -> UNDEFINED
    }
    val isEmpty: Boolean get() = itemModel == null

    init {
        this += backgroundImage.apply {
            setFillParent(true)
            setScaling(Scaling.contain)
        }

        slotItemBackgroundImage?.let { itemBackground ->
            this += itemBackground.apply {
                alpha = 0.33f
                setFillParent(true)
                setScaling(Scaling.contain)
            }
        }

        this += itemImage.apply {
            setFillParent(true)
            setScaling(Scaling.contain)
        }
    }

    override fun getPrefWidth() = backgroundImage.drawable.minWidth

    override fun getPrefHeight() = backgroundImage.drawable.minHeight

    fun putItem(model: ItemModel?) {
        itemModel = model
        itemImage.drawable = model?.let { skin.getDrawable(it.atlasKey) }
    }
}

package ru.aahzbrut.rpggame.ui.view.drag_drop

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.*
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target
import ru.aahzbrut.rpggame.data.ItemCategory
import ru.aahzbrut.rpggame.ui.model.ItemModel
import ru.aahzbrut.rpggame.ui.widget.InventorySlot

class InventoryDragSource(
    private val dnd: DragAndDrop,
    val inventorySlot: InventorySlot
) : Source(inventorySlot) {
    val isSourceSlotSpecial: Boolean get() = inventorySlot.supportedCategory != ItemCategory.UNDEFINED
    val supportedCategory: ItemCategory get() = inventorySlot.supportedCategory

    override fun dragStart(event: InputEvent?, x: Float, y: Float, pointer: Int): Payload? {
        return inventorySlot.itemModel?.let {
            val itemWidth = inventorySlot.itemImage.width
            val itemHeight = inventorySlot.itemImage.height

            dnd.setDragActorPosition(itemWidth * 0.5f, -itemHeight * 0.5f)

            Payload().apply {
                `object` = inventorySlot.itemModel
                dragActor = Image(inventorySlot.itemImage.drawable).apply {
                    setSize(itemWidth, itemHeight)
                }
                inventorySlot.putItem(null)
            }
        }
    }

    override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int, payload: Payload, target: Target?) {
        if (target == null) inventorySlot.putItem(payload.`object` as ItemModel)
    }
}

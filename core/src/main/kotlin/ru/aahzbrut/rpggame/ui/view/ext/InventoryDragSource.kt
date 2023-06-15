package ru.aahzbrut.rpggame.ui.view.ext

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.*
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target
import ru.aahzbrut.rpggame.ui.model.ItemModel
import ru.aahzbrut.rpggame.ui.widget.InventorySlot

class InventoryDragSource(
    private val dnd: DragAndDrop,
    val inventorySlot: InventorySlot
) : Source(inventorySlot) {

    override fun dragStart(event: InputEvent?, x: Float, y: Float, pointer: Int): Payload? {
        dnd.setDragActorPosition(10f, -10f)
        return inventorySlot.itemModel?.let {
            Payload().apply {
                `object` = inventorySlot.itemModel
                dragActor = Image(inventorySlot.itemImage.drawable).apply {
                    setSize(20f, 20f)
                }
                inventorySlot.putItem(null)
            }
        }
    }

    override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int, payload: Payload, target: Target?) {
        if (target == null) inventorySlot.putItem(payload.`object` as ItemModel)
    }
}

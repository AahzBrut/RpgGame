package ru.aahzbrut.rpggame.ui.view.drag_drop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.*
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target
import ru.aahzbrut.rpggame.data.ItemCategory
import ru.aahzbrut.rpggame.data.ItemCategory.UNDEFINED
import ru.aahzbrut.rpggame.ui.model.ItemModel
import ru.aahzbrut.rpggame.ui.widget.InventorySlot

class InventoryDragTarget(
    private val inventorySlot: InventorySlot,
    private val onDrop: (InventorySlot, InventorySlot, ItemModel) -> Unit,
) : Target(inventorySlot) {
    private val supportedCategory: ItemCategory = inventorySlot.supportedCategory
    private val isTargetSlotSpecial: Boolean get() = supportedCategory != UNDEFINED
    private val isTargetSlotContainsItem: Boolean get() = inventorySlot.itemModel != null
    private val targetInventoryItem: ItemModel? get() = inventorySlot.itemModel

    override fun drag(source: Source, payload: Payload, x: Float, y: Float, pointer: Int): Boolean {
        val itemModel = payload.`object` as ItemModel
        val itemCategory = itemModel.category
        val dragSource = source.asInventorySource()
        val result = !(isDraggedItemFitsTargetSlot(itemCategory) || isItemInTargetSlotFitsSourceSlot(dragSource))
        if (!result) payload.dragActor.color = Color.RED
        return result
    }

    override fun reset(source: Source, payload: Payload) {
        payload.dragActor.color = Color.WHITE
    }

    override fun drop(source: Source, payload: Payload, x: Float, y: Float, pointer: Int) {
        onDrop(
            (source as InventoryDragSource).inventorySlot,
            inventorySlot,
            payload.`object` as ItemModel
        )
    }

    private fun isItemInTargetSlotFitsSourceSlot(dragSource: InventoryDragSource) =
        dragSource.isSourceSlotSpecial && isTargetSlotContainsItem && targetInventoryItem?.category != dragSource.supportedCategory

    private fun isDraggedItemFitsTargetSlot(itemCategory: ItemCategory) =
        isTargetSlotSpecial && itemCategory != supportedCategory

    private fun Source.asInventorySource() = this as InventoryDragSource
}

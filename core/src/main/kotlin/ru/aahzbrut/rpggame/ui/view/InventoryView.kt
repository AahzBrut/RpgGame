package ru.aahzbrut.rpggame.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop
import com.badlogic.gdx.utils.Align
import ktx.scene2d.KTable
import ktx.scene2d.label
import ktx.scene2d.table
import ru.aahzbrut.rpggame.component.InventoryComponent
import ru.aahzbrut.rpggame.ui.get
import ru.aahzbrut.rpggame.ui.inventorySlot
import ru.aahzbrut.rpggame.ui.model.InventoryModel
import ru.aahzbrut.rpggame.ui.model.ItemModel
import ru.aahzbrut.rpggame.ui.resource.Drawables
import ru.aahzbrut.rpggame.ui.resource.Labels
import ru.aahzbrut.rpggame.ui.view.drag_drop.InventoryDragSource
import ru.aahzbrut.rpggame.ui.view.drag_drop.InventoryDragTarget
import ru.aahzbrut.rpggame.ui.widget.InventorySlot

class InventoryView(
    model: InventoryModel,
    skin: Skin,
) : Table(skin), KTable {
    private val inventorySlots = mutableListOf<InventorySlot>()
    private val equipmentSlots = mutableListOf<InventorySlot>()

    init {
        setFillParent(true)
        isVisible = false

        val titlePadding = 15f

        table { inventoryFrame ->
            background = skin[Drawables.FRAME_BACKGROUND]

            label(text = "Inventory", style = Labels.TITLE.skinKey, skin) {
                setAlignment(Align.center)
                it.expandX().fill()
                    .pad(-8f, titlePadding, 0f, titlePadding)
                    .top()
                    .row()
            }

            table { inventoryCells ->
                for (i in 1..InventoryComponent.CAPACITY) {
                    this@InventoryView.inventorySlots += inventorySlot(skin = skin) { slotCell ->
                        slotCell.padBottom(2f)
                        if (i % 6 == 0) {
                            slotCell.row()
                        } else {
                            slotCell.padRight(2f)
                        }
                    }
                }
                inventoryCells.expand().fill().padTop(-2f)
            }

            inventoryFrame.expand().width(158f).height(120f).left()
                .pad(0f, 24f, 0f, 0f)
        }

        table { equipmentFrame ->
            background = skin[Drawables.FRAME_BACKGROUND]
            label(text = "Gear", style = Labels.TITLE.skinKey, skin) {
                setAlignment(Align.center)
                it.expandX().fill()
                    .pad(-8f, titlePadding, 0f, titlePadding)
                    .top()
                    .row()
            }

            table { equipmentCells ->
                this@InventoryView.equipmentSlots += inventorySlot(Drawables.INVENTORY_SLOT_HELMET, skin) {
                    it.size(32f).padTop(-2f).padBottom(2f).colspan(3).row()
                }
                this@InventoryView.equipmentSlots += inventorySlot(Drawables.INVENTORY_SLOT_WEAPON, skin) {
                    it.size(32f).padTop(-2f).padBottom(2f)
                }

                table {
                    it.size(16f)
                }

                this@InventoryView.equipmentSlots += inventorySlot(Drawables.INVENTORY_SLOT_ARMOR, skin) {
                    it.size(32f).padTop(-2f).padBottom(2f).row()
                }
                this@InventoryView.equipmentSlots += inventorySlot(Drawables.INVENTORY_SLOT_BOOTS, skin) {
                    it.size(32f).padTop(-2f).padBottom(2f).colspan(3)
                }

                equipmentCells.expand().fill()
            }

            equipmentFrame.expand().width(100f).height(120f).right()
                .pad(0f, 0f, 0f, 24f)
        }

        initDragAndDrop()

        model.onPropertyChange(InventoryModel::playerItems){items->
            clearInventoryAndGear()
            items.forEach {item ->
                if (item.isEquipped) {
                    equipItem(item)
                } else {
                    putItemInInventory(item)
                }
            }
        }
    }

    fun putItemInInventory(model: ItemModel) {
        inventorySlots[model.slotIndex].putItem(model)
    }

    private fun equipItem(model: ItemModel) {
        equipmentSlots.first { it.supportedCategory == model.category }.run {
            putItem(model)
        }
    }

    private fun initDragAndDrop() {
        val dnd = DragAndDrop()
        dnd.dragTime = 15
        inventorySlots.forEach { slot ->
            dnd.addSource(InventoryDragSource(dnd, slot))
            dnd.addTarget(InventoryDragTarget(slot, ::onItemDropped))
        }
        equipmentSlots.forEach { slot ->
            dnd.addSource(InventoryDragSource(dnd, slot))
            dnd.addTarget(InventoryDragTarget(slot, ::onItemDropped))
        }

    }

    private fun onItemDropped(sourceSlot: InventorySlot, targetSlot: InventorySlot, itemModel: ItemModel) {
        if (!targetSlot.isEmpty) sourceSlot.putItem(targetSlot.itemModel)
        targetSlot.putItem(itemModel)
    }

    private fun clearInventoryAndGear() {
        inventorySlots.forEach { it.putItem(null) }
        equipmentSlots.forEach { it.putItem(null) }
    }
}

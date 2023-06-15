package ru.aahzbrut.rpggame.ui.model

import ru.aahzbrut.rpggame.data.ItemCategory

class ItemModel(
    val entityId: Int,
    val category: ItemCategory,
    val atlasKey: String,
    var slotIndex: Int,
    var isEquipped: Boolean
) : PropertyChangeNotifier() {
}

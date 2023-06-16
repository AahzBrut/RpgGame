package ru.aahzbrut.rpggame.ui.model

import com.github.quillraven.fleks.Entity
import ru.aahzbrut.rpggame.data.ItemCategory

class ItemModel(
    val entity: Entity,
    val category: ItemCategory,
    val atlasKey: String,
    var slotIndex: Int,
    var isEquipped: Boolean
)

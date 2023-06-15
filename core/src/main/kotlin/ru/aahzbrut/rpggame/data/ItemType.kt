package ru.aahzbrut.rpggame.data


enum class ItemType(
    val category: ItemCategory,
    val atlasKey: String
) {
    @Suppress("UNUSED")
    NOTHING(ItemCategory.UNDEFINED, ""),
    HELMET(ItemCategory.HELMET, "helmet"),
    ARMOR(ItemCategory.ARMOR, "armor"),
    SWORD(ItemCategory.WEAPON, "sword"),
    BIG_SWORD(ItemCategory.WEAPON, "sword2"),
    BOOTS(ItemCategory.BOOTS, "boots"),
}

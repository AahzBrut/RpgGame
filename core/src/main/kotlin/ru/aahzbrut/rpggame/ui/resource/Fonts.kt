package ru.aahzbrut.rpggame.ui.resource

enum class Fonts(val regionName: String, val scale: Float) {
    DEFAULT("fnt_white", 0.25f);

    val skinKey = "Font_${this.name.lowercase()}"
    val fontPath = "ui/${this.regionName}.fnt"
}

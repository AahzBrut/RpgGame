package ru.aahzbrut.rpggame.data

data class AnimationId(
    val model: AnimationModel,
    val type: AnimationType,
    val facing: FacingType,
){
    val regionName: String get() = "${model.typeName}${type.atlasKey}${facing.atlasKey}"
}

package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import ktx.assets.disposeSafely
import ktx.tiled.propertyOrNull
import ru.aahzbrut.rpggame.event_bus.EventBus
import ru.aahzbrut.rpggame.event_bus.event.MapChangedEvent
import ru.aahzbrut.rpggame.event_bus.event.SoundEffectEvent

class AudioSystem(
    eventBus: EventBus = inject()
) : IntervalSystem() {
    private val musicCache = mutableMapOf<String, Music>()
    private val soundCache = mutableMapOf<String, Sound>()
    private val soundQueue = mutableSetOf<String>()

    init {
        eventBus.onEvent(::handleMapChangedEvent)
        eventBus.onEvent(::handleSoundEffectEvent)
    }

    private fun handleMapChangedEvent(event: MapChangedEvent) {
        event.map.propertyOrNull<String>("music")?.let { path ->
            val music = musicCache.getOrPut(path) {
                Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                    isLooping = true
                    volume = 0.3f
                }
            }
            music.play()
        }
    }

    private fun handleSoundEffectEvent(event: SoundEffectEvent) {
        queueSound("audio/effects/${event.model.typeName}_${event.effectType.key}.wav")
    }

    override fun onTick() {
        soundQueue.forEach { path ->
            val soundEffect = soundCache.getOrPut(path) {
                Gdx.audio.newSound(Gdx.files.internal(path))
            }
            soundEffect.play()
        }
        soundQueue.clear()
    }

    override fun onDispose() {
        musicCache.values.forEach { it.disposeSafely() }
        soundCache.values.forEach { it.disposeSafely() }
    }

    private fun queueSound(path: String) {
        soundQueue.add(path)
    }
}

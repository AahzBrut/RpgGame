package ru.aahzbrut.rpggame.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely
import ktx.tiled.propertyOrNull
import ru.aahzbrut.rpggame.event.SoundEffectEvent
import ru.aahzbrut.rpggame.event.MapChangedEvent

class AudioSystem : EventListener, IntervalSystem() {
    companion object {
        val logger = ktx.log.logger<AudioSystem>()
    }

    private val musicCache = mutableMapOf<String, Music>()
    private val soundCache = mutableMapOf<String, Sound>()
    private val soundQueue = mutableSetOf<String>()

    override fun handle(event: Event): Boolean {
        when (event) {
            is MapChangedEvent -> {
                event.map.propertyOrNull<String>("music")?.let { path ->
                    logger.debug { "Music file path: $path" }
                    val music = musicCache.getOrPut(path) {
                        Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                            this.isLooping = true
                        }
                    }
                    music.play()
                }
                return true
            }

            is SoundEffectEvent -> queueSound("audio/effects/${event.model.typeName}_${event.effectType.key}.wav")
        }

        return false
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

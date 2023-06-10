package ru.aahzbrut.rpggame

import com.badlogic.gdx.physics.box2d.Fixture
import com.github.quillraven.fleks.Entity

val Fixture.entity: Entity get() = this.body.userData as Entity

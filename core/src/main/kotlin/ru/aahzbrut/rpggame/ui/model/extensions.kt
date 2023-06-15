package ru.aahzbrut.rpggame.ui.model

inline fun <reified T : Any> propertyNotify(initialValue: T): PropertyChangeDelegate<T> = PropertyChangeDelegate(initialValue)

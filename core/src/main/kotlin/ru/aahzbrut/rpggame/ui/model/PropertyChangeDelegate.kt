package ru.aahzbrut.rpggame.ui.model

import kotlin.reflect.KProperty

class PropertyChangeDelegate<T : Any>(initialValue: T) {
    private var _value: T = initialValue

    operator fun getValue(thisRef: PropertyChangeNotifier, property: KProperty<*>): T = _value

    operator fun setValue(thisRef: PropertyChangeNotifier, property: KProperty<*>, value: T) {
        _value = value
        thisRef.notify(property, value)
    }
}

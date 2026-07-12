package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.ui.Messages

abstract class PersonRelationalGraphic : PersonRelationalComponent() {
    enum class Orientation {
        TOP2BOTTOM,
        LEFT2RIGHT,
        BOTTOM2TOP,
        RIGHT2LEFT,
    }

    var orientation: Orientation? = null
        set(newOrientation) {
            if (field != newOrientation) {
                field = newOrientation
                rebuild()
            }
        }

    var generations: Int = 4
        set(newGenerations) {
            if (field != newGenerations) {
                require(newGenerations >= 1) {
                    Messages.getString("PersonRelationalGraphic.GenerationNumTooLow")
                }
                field = newGenerations
                rebuild()
            }
        }
}

package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.PersonRelationalComponent as PersonRelationalComponentInterface
import fr.cdbs.oegen.ui.PersonRelationalComponent.Scope
import javax.swing.JComponent

abstract class PersonRelationalComponent :
    JComponent(),
    PersonRelationalComponentInterface {

    final override var center: Person? = null
        set(newCenter) {
            if (field != newCenter) {
                field = newCenter
                rebuild()
            }
        }

    final override var scope: Scope? = null
        set(newScope) {
            if (field != newScope) {
                field = newScope
                rebuild()
            }
        }

    protected abstract fun rebuild()
}

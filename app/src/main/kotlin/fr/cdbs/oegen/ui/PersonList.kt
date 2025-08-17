/**
 *
 */
package fr.cdbs.oegen.ui

import fr.cdbs.oegen.data.Person
import java.util.*
import javax.swing.DefaultListModel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

/**
 * @author toxn
 */
open class PersonList

    : DefaultListModel<Person>(), ChangeListener, Iterable<Person> {
    /**
     * @author toxn
     */
    inner class PersonListIterator : MutableIterator<Person> {
        var enumeration: Enumeration<Person> = elements()

        /*
	 * !CodeTemplates.overridecomment.nonjd!
	 *
	 * @see java.util.Iterator#hasNext()
	 */
        override fun hasNext(): Boolean {
            return enumeration.hasMoreElements()
        }

        /*
	 * !CodeTemplates.overridecomment.nonjd!
	 *
	 * @see java.util.Iterator#next()
	 */
        override fun next(): Person {
            return enumeration.nextElement()
        }

        override fun remove() {
            TODO("Not yet implemented")
        }
    }

    override fun iterator(): MutableIterator<Person> {
        return PersonListIterator()
    }

    fun search(id: String?): Person? {
        // FIXME: in dire need of a hashmap
        for (p in this) if (p.id == id) return p

        return null
    }

    override fun stateChanged(arg0: ChangeEvent) {
        // Get the index of the object triggering the event
        val changedIndex = this.indexOf(arg0.getSource())

        // and forward the change to all the listeners
        for (ldl in listenerList.getListeners(ListDataListener::class.java)) {
            ldl.contentsChanged(ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, changedIndex, changedIndex))
        }
    }
}

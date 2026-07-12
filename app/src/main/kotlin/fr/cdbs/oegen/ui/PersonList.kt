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
        if (id.isNullOrEmpty()) return null
        return Person.indexId[id]
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

fun PersonList.toList(): List<Person> = (0 until size).map { getElementAt(it)!! }

fun PersonList.searchByName(query: String): List<Person> {
    val tokens = query.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    if (tokens.isEmpty()) return toList()

    return filter { person ->
        val first = person.firstName.orEmpty()
        val last = person.lastName.orEmpty()
        val fullName = "$first $last $last $first"
        tokens.all { token ->
            first.contains(token, ignoreCase = true) ||
                last.contains(token, ignoreCase = true) ||
                fullName.contains(token, ignoreCase = true)
        }
    }
}

enum class PersonSortOrder {
    LAST_FIRST,
    FIRST_LAST,
}

fun List<Person>.sortedByPersonOrder(order: PersonSortOrder): List<Person> = when (order) {
    PersonSortOrder.LAST_FIRST -> sortedWith(
        compareBy({ it.lastName.orEmpty().lowercase() }, { it.firstName.orEmpty().lowercase() }),
    )
    PersonSortOrder.FIRST_LAST -> sortedWith(
        compareBy({ it.firstName.orEmpty().lowercase() }, { it.lastName.orEmpty().lowercase() }),
    )
}

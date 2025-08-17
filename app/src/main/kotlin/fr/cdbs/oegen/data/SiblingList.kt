package fr.cdbs.oegen.data

import fr.cdbs.oegen.ui.PersonList
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

/**
 *
 */
/**
 * @author toxn
 */
class SiblingList(private val person: Person) : PersonList(), ListDataListener {
    init {
        rebuildList()
    }

    override fun contentsChanged(e: ListDataEvent?) {
        // Forward event to listeners

        fireContentsChanged(e, size, size - 1)
    }

    override fun intervalAdded(e: ListDataEvent) {
        val source = e.getSource() as PersonList

        for (i in e.index0..e.index1) {
            val p = source.elementAt(i)

            if (p !in this && p != person) {
                addElement(p)
            }
        }
    }

    override fun intervalRemoved(e: ListDataEvent?) {
        val father = person.father
        val mother = person.mother

        father?.children?.removeListDataListener(this)

        mother?.children?.removeListDataListener(this)

        clear()
        fireIntervalRemoved(this, 0, size - 1)

        rebuildList()
    }

    private fun rebuildList() {
        val father = person.father
        val mother = person.mother

        father?.children?.addListDataListener(this)
        for (p in father?.children!!) if (p !in this && p != person) {
            addElement(p)
        }

        if (mother != null) {
            mother.children.addListDataListener(this)
            for (p in mother.children) if (p !in this && p != person) {
                addElement(p)
            }
        }

        fireIntervalAdded(this, 0, size - 1)
    }
}
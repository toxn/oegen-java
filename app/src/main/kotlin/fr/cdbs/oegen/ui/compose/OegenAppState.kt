package fr.cdbs.oegen.ui.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.db.DatabaseService
import fr.cdbs.oegen.ui.PersonSortOrder
import fr.cdbs.oegen.ui.searchByName
import fr.cdbs.oegen.ui.sortedByPersonOrder
import fr.cdbs.oegen.ui.toList
import java.io.File
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class OegenAppState {
    var selectedPerson by mutableStateOf<Person?>(null)
        private set

    var searchQuery by mutableStateOf("")
    var sortOrder by mutableStateOf(PersonSortOrder.LAST_FIRST)
    var isDirty by mutableStateOf(false)
    /** Last XML file used for import or export (interchange); primary store is the database. */
    var saveFile by mutableStateOf<File?>(null)

    private var listRevision by mutableIntStateOf(0)
    val personsRevision: Int get() = listRevision

    private val listListener = object : ListDataListener {
        override fun contentsChanged(e: ListDataEvent) = onListChanged()
        override fun intervalAdded(e: ListDataEvent) = onListChanged()
        override fun intervalRemoved(e: ListDataEvent) = onListChanged()

        private fun onListChanged() {
            listRevision++
            isDirty = true
            if (Person.persons.isEmpty()) {
                selectedPerson = null
            } else if (selectedPerson != null && selectedPerson !in Person.persons.toList()) {
                selectedPerson = Person.persons.toList().firstOrNull()
            }
        }
    }

    init {
        Person.persons.addListDataListener(listListener)
    }

    fun selectPerson(person: Person?) {
        selectedPerson = person
    }

    fun filteredPersons(): List<Person> {
        listRevision // observe list changes
        val query = searchQuery.trim()
        val base = if (query.isEmpty()) {
            Person.persons.toList()
        } else {
            Person.persons.searchByName(query)
        }
        return base.sortedByPersonOrder(sortOrder)
    }

    fun markSaved(file: File? = saveFile) {
        saveFile = file
        isDirty = false
    }

    fun markDirty() {
        isDirty = true
    }

    fun createPerson(): Person {
        val person = Person()
        selectPerson(person)
        return person
    }

    fun removePersons(persons: Collection<Person>) {
        persons.forEach { it.remove() }
    }

    val windowTitle: String
        get() {
            val base = "\u0152gen"
            val dbPart = DatabaseService.currentConfig().displayName.let { " — $it" }
            val dirtyPart = if (isDirty) " *" else ""
            return base + dbPart + dirtyPart
        }
}

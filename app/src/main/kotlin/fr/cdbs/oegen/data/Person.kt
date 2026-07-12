package fr.cdbs.oegen.data

import fr.cdbs.oegen.data.date.FuzzyDate
import fr.cdbs.oegen.data.date.LifeEventKind
import fr.cdbs.oegen.ui.Messages
import fr.cdbs.oegen.ui.PersonList
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import java.util.HashMap
import java.util.UUID
import javax.swing.event.ChangeEvent

/**
 *
 */
/**
 * @author toxn
 */
class Person {
    enum class Gender(val nlsName: String, @JvmField val symbol: String) {
        Unknown(Messages.getString("Person.Gender.Unknown"), "?"),  //$NON-NLS-2$
        Male(Messages.getString("Person.Gender.Male"), "\u2642"),  //$NON-NLS-2$
        Female(Messages.getString("Person.Gender.Female"), "\u2640"),  //$NON-NLS-2$
        Other(Messages.getString("Person.Gender.Other"), "\u26A7"); //$NON-NLS-2$

        override fun toString(): String {
            return nlsName
        }
    }

    var siblings: SiblingList? = null
        get() {
            if (field == null) {
                field = SiblingList(this)
            }

            return field
        }
        private set

    val pcs: PropertyChangeSupport = PropertyChangeSupport(this)

    @JvmField
    var children: PersonList = PersonList()

    /**
     * @return le gender
     */
    var gender: Gender? = Gender.Unknown
        set(newValue) {
            val oldValue = field
            field = newValue

            pcs.firePropertyChange(PROPERTY_GENDER, oldValue, newValue)

            persons.stateChanged(ChangeEvent(this))
        }

    var firstName: String? = ""
        set(newValue) {
            val oldValue = field
            field = newValue
            pcs.firePropertyChange(PROPERTY_FIRSTNAME, oldValue, newValue)
            persons.stateChanged(ChangeEvent(this))
        }
    
    var lastName: String? = "" //$NON-NLS-1$
        set(newValue) {
            val oldValue = field
            field = newValue
            pcs.firePropertyChange(PROPERTY_LASTNAME, oldValue, newValue)
            persons.stateChanged(ChangeEvent(this))
        }
    
    /**
     * @return the father
     */
    var father: Person? = null
        set(newValue) {
            if (field === newValue) return

            field?.children?.removeElement(this)

            val oldValue = field

            field = newValue

            pcs.firePropertyChange(PROPERTY_FATHER, oldValue, newValue)

            field?.children?.addElement(this)
            field?.children?.stateChanged(ChangeEvent(this))
            
            persons.stateChanged(ChangeEvent(this))
        }

    /**
     * @return the mother
     */
    var mother: Person? = null
        set(newValue) {
            if (field === newValue) return

            if (field != null) {
                field!!.children.removeElement(this)
            }

            val oldValue = field
            field = newValue
            pcs.firePropertyChange(PROPERTY_MOTHER, oldValue, newValue)

            field?.children?.addElement(this)
            field?.children?.stateChanged(ChangeEvent(this))

            persons.stateChanged(ChangeEvent(this))
        }

    var id: String? = null
        set(newValue) {
            indexId.remove(id)
    
            val oldId = field
    
            field = newValue ?: (GENERATED_ID_PREFIX + UUID.randomUUID())
            indexId[field!!] = this

            pcs.firePropertyChange(PROPERTY_ID, oldId, field)
            persons.stateChanged(ChangeEvent(this))
        }

    init {
        persons.addElement(this)
        id = null
    }


    var lifeEvents: PersonLifeEvents = PersonLifeEvents()
        set(newValue) {
            val oldValue = field
            field = newValue
            pcs.firePropertyChange(PROPERTY_LIFE_EVENTS, oldValue, newValue)
            persons.stateChanged(ChangeEvent(this))
        }

    var birthDate: FuzzyDate?
        get() = lifeEvents.birth
        set(value) = setLifeEventDate(LifeEventKind.BIRTH, value)

    var baptismDate: FuzzyDate?
        get() = lifeEvents.baptism
        set(value) = setLifeEventDate(LifeEventKind.BAPTISM, value)

    var marriageDate: FuzzyDate?
        get() = lifeEvents.marriage
        set(value) = setLifeEventDate(LifeEventKind.MARRIAGE, value)

    var deathDate: FuzzyDate?
        get() = lifeEvents.death
        set(value) = setLifeEventDate(LifeEventKind.DEATH, value)

    var funeralDate: FuzzyDate?
        get() = lifeEvents.funeral
        set(value) = setLifeEventDate(LifeEventKind.FUNERAL, value)

    private fun setLifeEventDate(kind: LifeEventKind, value: FuzzyDate?) {
        val events = lifeEvents.copy()
        events.set(kind, value)
        lifeEvents = events
    }

    fun addPropertyChangeListener(listener: PropertyChangeListener?) {
        pcs.addPropertyChangeListener(listener)
    }

    /**
     * Delete the person from the database
     *
     * This will cleanly erase the person and forward its deletion to each
     * listener.
     */
    fun remove() {
        // unlink from parents.
        this.father = null
        this.mother = null

        // unlink from children
        for (child in children) {
            if (child?.father === this) {
                child.father = null
            }

            if (child?.mother === this) {
                child.mother = null
            }
        }

        // Clear the listeners
        for (pcl in pcs.propertyChangeListeners) {
            pcs.removePropertyChangeListener(pcl)
        }

        persons.removeElement(this)

        // dereference without triggering stateChanged in persons
        firstName = null
        lastName = null

        indexId.remove(id)
        id = null
    }

    fun removePropertyChangeListener(listener: PropertyChangeListener?) {
        pcs.removePropertyChangeListener(listener)
    }

    /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
    override fun toString(): String {
        return "$firstName $lastName" //$NON-NLS-1$
    }

    companion object {
        const val GENERATED_ID_PREFIX: String = "$"
        @JvmField
        val persons: PersonList = PersonList()


        val indexId: HashMap<String, Person> = HashMap<String, Person>()

        const val PROPERTY_FATHER: String = "father"
        const val PROPERTY_FIRSTNAME: String = "firstName"

        const val PROPERTY_GENDER: String = "gender" //$NON-NLS-1$

        const val PROPERTY_LASTNAME: String = "lastName" //$NON-NLS-1$
        const val PROPERTY_MOTHER: String = "mother" //$NON-NLS-1$

        const val PROPERTY_ID: String = "id" //$NON-NLS-1$

        const val PROPERTY_LIFE_EVENTS: String = "lifeEvents" //$NON-NLS-1$


        const val CLASSNAME: String = "person" //$NON-NLS-1$

        const val PROPERTY_CHILDREN: String = "children" //$NON-NLS-1$

        const val TAG_CHILD: String = "child" //$NON-NLS-1$

        @JvmStatic
        fun clearAll() {
            persons.clear()
            indexId.clear()
        }
    }
}
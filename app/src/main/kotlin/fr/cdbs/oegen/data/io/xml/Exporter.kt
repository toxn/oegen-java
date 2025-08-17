package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.Person.Gender
import fr.cdbs.oegen.data.io.Exporter
import fr.cdbs.oegen.ui.Messages
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.OutputStream
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class Exporter(outputStream: OutputStream?) : Exporter(outputStream) {
    var doc: Document? = null

    var exportedPersons: HashMap<Person?, Element> = HashMap<Person?, Element>()

    override fun doExport() {
        try {
            doc = XmlIOContext.Companion.getDocument(null)
        } catch (e1: Exception) {
            throw RuntimeException(Messages.getString("Exporter.internalError"), e1) //$NON-NLS-1$
        }

        val db = doc!!.createElement(XmlIOContext.Companion.DB_NAME)
        db.setAttribute(XmlIOContext.Companion.DB_SCHEMA_W3C_PREFIX, XmlIOContext.Companion.DB_SCHEMA_W3C_URI)
        db.setAttribute(XmlIOContext.Companion.DB_SCHEMA_OEGEN_PREFIX, XmlIOContext.Companion.DB_SCHEMA_OEGEN_URI)
        doc!!.appendChild(db)

        for (p in Person.persons) {
            if (!isComplete && exportedPersons.containsKey(p)) {
                // Person is already exported, let's skip it
                continue
            }

            db.appendChild(exportPerson(p!!, null))
        }

        val transformerFactory = TransformerFactory.newInstance()
        try {
            val transformer = transformerFactory.newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes") //$NON-NLS-1$
            val source = DOMSource(doc)
            val result = StreamResult(this@Exporter.outputStream)
            transformer.transform(source, result)
        } catch (e: TransformerException) {
            // TODO Bloc catch généré automatiquement
            e.printStackTrace()
        }
    }

    /**
     * Add a Person to an element
     * @param person person to add
     */
    fun exportPerson(person: Person, source: Person?): Element {
        val personElement: Element
        val id = person.id
        val firstName = person.firstName
        val lastName = person.lastName
        val gender = person.gender
        val father = person.father
        val mother = person.mother

        if (exportedPersons.containsKey(person)) {
            personElement = doc!!.createElement(XmlIOContext.Companion.PERSONREF_NAME)
            val ep: Element? = exportedPersons[person]
            ep?.hasAttribute(Person.PROPERTY_ID)?.let {
                if (!it) {
                    ep.setAttribute(Person.PROPERTY_ID, id)
                }
            }

            personElement.setAttribute(XmlIOContext.Companion.PERSONREF_ATTR_REF, id)
        } else {
            personElement = doc!!.createElement(Person.CLASSNAME)
            if (!id!!.startsWith(Person.GENERATED_ID_PREFIX)) {
                personElement.setAttribute(Person.PROPERTY_ID, id)
            }
            exportedPersons.put(person, personElement)

            if (firstName != null) {
                val firstNameElement = doc!!.createElement(Person.PROPERTY_FIRSTNAME)
                firstNameElement.setTextContent(firstName)
                personElement.appendChild(firstNameElement)
            }

            if (lastName != null) {
                val lastNameElement = doc!!.createElement(Person.PROPERTY_LASTNAME)
                lastNameElement.setTextContent(lastName)
                personElement.appendChild(lastNameElement)
            }

            if (gender != Gender.Unknown) {
                val genderElement = doc!!.createElement(Person.PROPERTY_GENDER)
                genderElement.setTextContent(gender.toString())
                personElement.appendChild(genderElement)
            }

            if (father != null && (father != source || isComplete)) {
                val fatherElement = exportPerson(person.father!!, person)
                fatherElement.setAttribute(XmlIOContext.Companion.PERSON_ATTR_RELATION, Person.PROPERTY_FATHER)
                personElement.appendChild(fatherElement)
            }

            if (mother != null && (source != mother || isComplete)) {
                val motherElement = exportPerson(person.mother!!, person)
                motherElement.setAttribute(XmlIOContext.Companion.PERSON_ATTR_RELATION, Person.PROPERTY_MOTHER)
                personElement.appendChild(motherElement)
            }

            /*
	     * In case we are in complete mode, children list is appened even if
	     * it's empty. If else, children list is not appened whenever it is
	     * empty or it only contains the parent node (ie source).
	     */
            if (isComplete || !(person.children.isEmpty()
                        || person.children.size() == 1 && person.children.contains(source))
            ) {
                if (!isComplete && person.children.size() > (if (person.children.contains(source)) 2 else 1)) {
                    for (child in person.children) {
                        if (child != source) {
                            val childElement = exportPerson(child!!, person)
                            childElement.setAttribute(XmlIOContext.Companion.PERSON_ATTR_RELATION, XmlIOContext.Companion.P2PRELTYPE_CHILD)
                            personElement.appendChild(childElement)
                        }
                    }
                } else {
                    val childrenElement = doc!!.createElement(XmlIOContext.Companion.LISTOF_NAME)
                    personElement.appendChild(childrenElement)

                    childrenElement.setAttribute(XmlIOContext.Companion.LISTOF_ATTR_OF, XmlIOContext.Companion.P2PRELTYPE_CHILD)

                    for (child in person.children) {
                        if (child != source || isComplete) {
                            childrenElement.appendChild(exportPerson(child!!, person))
                        }
                    }
                }
            }
        }

        return personElement
    }
}

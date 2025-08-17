package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.Person.Gender
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.DB_NAME
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.LISTOF_ATTR_OF
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.LISTOF_NAME
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.P2PRELTYPE_CHILD
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.P2PRELTYPE_FATHER
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.P2PRELTYPE_MOTHER
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.P2PRELTYPE_PARENT
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.PERSONREF_ATTR_REF
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.PERSONREF_NAME
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.PERSON_ATTR_RELATION
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.PERSON_FIRSTNAME
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.PERSON_GENDER
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.PERSON_LASTNAME
import fr.cdbs.oegen.data.io.xml.XmlIOContext.Companion.PERSON_NAME
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.IOException
import java.io.InputStream
import java.text.MessageFormat

class Importer(`is`: InputStream?) : fr.cdbs.oegen.data.io.Importer(`is`) {
    override fun doImport() {
        try {
            val nl: NodeList? = XmlIOContext.Companion.getDocument(inputStream)?.childNodes


            for (i in 0..<(nl?.length ?:0)) {
                val n = nl?.item(i)

                if (n?.nodeType == Node.ELEMENT_NODE) {
                    if (n.nodeName === DB_NAME) {
                        importDB(n as Element)
                        // every following nodes are ignored
                        return
                    }
                }
            }
        } catch (e: SAXException) {
            // TODO Bloc catch généré automatiquement
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Bloc catch généré automatiquement
            e.printStackTrace()
        }
    }

    companion object {
        private fun createRelation(source: Person, destination: Person, relation: String) {
            when (relation) {
                P2PRELTYPE_CHILD -> source.children.addElement(destination)
                P2PRELTYPE_FATHER -> source.father = destination
                P2PRELTYPE_MOTHER -> source.mother = destination
                P2PRELTYPE_PARENT -> when (destination.gender) {
                    Gender.Male -> source.father = destination
                    Gender.Female -> source.mother = destination
                    else -> throw RuntimeException(Messages.getString("Importer.ParentWithoutGender")) //$NON-NLS-1$

                }
            }
        }

        private fun importDB(element: Element) {
            assert(element.nodeName === DB_NAME)

            var child = element.firstChild

            while (child != null) {
                if (child.nodeType == Node.ELEMENT_NODE) {
                    when (child.nodeName) {
                        PERSON_NAME -> importPerson(child as Element)
                    }
                }

                child = child.nextSibling
            }
            return
        }

        private fun importPerson(element: Element): Person {
            val currentPerson = Person()
            var subPerson: Person?

            val id = element.getAttribute(XmlIOContext.Companion.OEBJECT_ID)
            if (id.isNotEmpty()) {
                currentPerson.id = id
            }

            val nl = element.childNodes

            for (i in 0..<nl.length) {
                val n = nl.item(i)

                if (n.nodeType == Node.ELEMENT_NODE) {
                    val e = n as Element

                    when (e.nodeName) {
                        PERSON_FIRSTNAME -> currentPerson.firstName = e.textContent
                        PERSON_LASTNAME -> currentPerson.lastName = e.textContent
                        PERSON_GENDER -> currentPerson.gender = Gender.valueOf(e.textContent)
                        PERSON_NAME -> {
                            subPerson = importPerson(e)

                            when (e.getAttribute(PERSON_ATTR_RELATION)) {
                                Person.PROPERTY_FATHER -> currentPerson.father = subPerson
                                Person.PROPERTY_MOTHER -> currentPerson.mother = subPerson
                                Person.TAG_CHILD -> currentPerson.children.addElement(subPerson)
                                else -> throw RuntimeException(
                                    MessageFormat.format(
                                        "",  //$NON-NLS-1$
                                        e.getAttribute(PERSON_ATTR_RELATION)
                                    )
                                )
                            }
                        }

                        PERSONREF_NAME -> createRelation(
                            currentPerson,
                            Person.persons.search(e.getAttribute(PERSONREF_ATTR_REF))!!,
                            e.getAttribute(LISTOF_ATTR_OF)
                        )

                        LISTOF_NAME -> {
                            val relation = e.getAttribute(LISTOF_ATTR_OF)

                            var nodeFromList = e.firstChild

                            while (nodeFromList != null) {
                                if (nodeFromList.nodeType == Node.ELEMENT_NODE) {
                                    val elFromList = nodeFromList as Element
                                    when (nodeFromList.nodeName) {
                                        PERSON_NAME -> createRelation(currentPerson, importPerson(elFromList), relation)
                                        PERSONREF_NAME -> createRelation(
                                            currentPerson,
                                            Person.persons.search(elFromList.getAttribute(PERSONREF_ATTR_REF))!!,
                                            relation
                                        )
                                    }
                                }

                                nodeFromList = nodeFromList.nextSibling
                            }
                        }
                    }
                }
            }

            return currentPerson
        }
    }
}

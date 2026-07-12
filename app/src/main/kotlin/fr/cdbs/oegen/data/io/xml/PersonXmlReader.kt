package fr.cdbs.oegen.data.io.xml

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.Person.Gender
import fr.cdbs.oegen.data.io.xml.model.OegenDbDocument
import fr.cdbs.oegen.data.io.xml.model.PersonNode
import fr.cdbs.oegen.data.io.xml.model.PersonRefNode
import fr.cdbs.oegen.data.io.xml.model.RelationListNode
import java.text.MessageFormat

object PersonXmlReader {
    fun importDocument(document: OegenDbDocument) {
        document.persons.forEach(::importPerson)
    }

    private fun importPerson(node: PersonNode): Person {
        val person = Person()

        node.id?.takeIf { it.isNotEmpty() }?.let { person.id = it }
        node.firstName?.let { person.firstName = it }
        node.lastName?.let { person.lastName = it }
        node.gender?.let { person.gender = Gender.valueOf(it) }

        node.nestedPersons.orEmpty().forEach { nested ->
            val related = importPerson(nested)
            when (nested.relation) {
                Person.PROPERTY_FATHER -> person.father = related
                Person.PROPERTY_MOTHER -> person.mother = related
                Person.TAG_CHILD -> person.children.addElement(related)
                else -> throw unknownRelation(nested.relation)
            }
        }

        node.lists.orEmpty().forEach { list ->
            importListEntries(person, list)
        }

        node.personRefs.orEmpty().forEach { ref ->
            createRelation(person, resolveRef(ref.ref), ref.of.orEmpty())
        }

        return person
    }

    private fun importListEntries(person: Person, list: RelationListNode) {
        list.persons.orEmpty().forEach { child ->
            createRelation(person, importPerson(child), list.of)
        }
        list.personRefs.orEmpty().forEach { ref ->
            createRelation(person, resolveRef(ref.ref), list.of)
        }
    }

    private fun createRelation(source: Person, destination: Person, relation: String) {
        when (relation) {
            XmlConstants.P2PRELTYPE_CHILD -> source.children.addElement(destination)
            XmlConstants.P2PRELTYPE_FATHER -> source.father = destination
            XmlConstants.P2PRELTYPE_MOTHER -> source.mother = destination
            XmlConstants.P2PRELTYPE_PARENT -> when (destination.gender) {
                Gender.Male -> source.father = destination
                Gender.Female -> source.mother = destination
                else -> throw RuntimeException(Messages.getString("Importer.ParentWithoutGender"))
            }
            else -> throw unknownRelation(relation)
        }
    }

    private fun resolveRef(ref: String): Person {
        return Person.persons.search(ref)
            ?: throw RuntimeException("Unknown person reference: $ref")
    }

    private fun unknownRelation(relation: String?): RuntimeException {
        return RuntimeException(
            MessageFormat.format(Messages.getString("XmlImporter.UnkownRelationType"), relation),
        )
    }
}

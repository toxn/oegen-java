package fr.cdbs.oegen.data.io

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.Person.Gender
import fr.cdbs.oegen.data.io.model.OegenDbDocument
import fr.cdbs.oegen.data.io.model.PersonNode
import fr.cdbs.oegen.data.io.model.PersonRefNode
import fr.cdbs.oegen.data.io.model.RelationListNode
import java.text.MessageFormat

object PersonGraphReader {
    fun importDocument(document: OegenDbDocument) {
        document.persons.forEach(::importPerson)
    }

    private fun importPerson(node: PersonNode): Person {
        val person = Person()

        node.id?.takeIf { it.isNotEmpty() }?.let { person.id = it }
        node.firstName?.let { person.firstName = it }
        node.lastName?.let { person.lastName = it }
        node.gender?.let { person.gender = Gender.valueOf(it) }

        node.birth?.let { person.birthDate = it }
        node.baptism?.let { person.baptismDate = it }
        node.marriage?.let { person.marriageDate = it }
        node.death?.let { person.deathDate = it }
        node.funeral?.let { person.funeralDate = it }

        node.birthPlace?.let { person.birthPlace = it }
        node.baptismPlace?.let { person.baptismPlace = it }
        node.marriagePlace?.let { person.marriagePlace = it }
        node.deathPlace?.let { person.deathPlace = it }
        node.funeralPlace?.let { person.funeralPlace = it }

        node.alternateNames.orEmpty().forEach { alternate ->
            person.alternateNames = person.alternateNames + AlternateNameIo.fromNode(alternate)
        }

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
            IoConstants.P2PRELTYPE_CHILD -> source.children.addElement(destination)
            IoConstants.P2PRELTYPE_FATHER -> source.father = destination
            IoConstants.P2PRELTYPE_MOTHER -> source.mother = destination
            IoConstants.P2PRELTYPE_PARENT -> when (destination.gender) {
                Gender.Male -> source.father = destination
                Gender.Female -> source.mother = destination
                else -> throw RuntimeException(IoMessages.getString("Importer.ParentWithoutGender"))
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
            MessageFormat.format(IoMessages.getString("XmlImporter.UnkownRelationType"), relation),
        )
    }
}

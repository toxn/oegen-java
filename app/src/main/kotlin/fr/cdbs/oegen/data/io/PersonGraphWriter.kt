package fr.cdbs.oegen.data.io

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.Person.Gender
import fr.cdbs.oegen.data.io.model.OegenDbDocument
import fr.cdbs.oegen.data.io.model.PersonNode
import fr.cdbs.oegen.data.io.model.PersonRefNode
import fr.cdbs.oegen.data.io.model.RelationListNode

object PersonGraphWriter {
    fun exportDatabase(isComplete: Boolean): OegenDbDocument {
        val exportedPersons = HashMap<Person, PersonNode>()
        val persons = mutableListOf<PersonNode>()
        for (person in Person.persons) {
            if (!isComplete && exportedPersons.containsKey(person)) {
                continue
            }
            when (val exported = exportPerson(person, source = null, isComplete, exportedPersons)) {
                is ExportedPerson.Full -> persons += exported.node
                is ExportedPerson.Reference -> persons += personRefNode(exported.ref)
            }
        }
        return OegenDbDocument(persons = persons)
    }

    private sealed class ExportedPerson {
        data class Full(val node: PersonNode) : ExportedPerson()
        data class Reference(val ref: PersonRefNode) : ExportedPerson()
    }

    private fun exportPerson(
        person: Person,
        source: Person?,
        isComplete: Boolean,
        exportedPersons: MutableMap<Person, PersonNode>,
    ): ExportedPerson {
        exportedPersons[person]?.let { existing ->
            ensureExportedId(existing, person, exportedPersons)
            return ExportedPerson.Reference(PersonRefNode(ref = person.id!!))
        }

        val node = PersonNode(
            id = person.id?.takeUnless { it.startsWith(Person.GENERATED_ID_PREFIX) },
            firstName = person.firstName,
            lastName = person.lastName,
            gender = person.gender?.takeUnless { it == Gender.Unknown }?.name,
            birth = person.birthDate,
            baptism = person.baptismDate,
            marriage = person.marriageDate,
            death = person.deathDate,
            funeral = person.funeralDate,
            birthPlace = person.birthPlace,
            baptismPlace = person.baptismPlace,
            marriagePlace = person.marriagePlace,
            deathPlace = person.deathPlace,
            funeralPlace = person.funeralPlace,
        )
        exportedPersons[person] = node

        val nestedPersons = mutableListOf<PersonNode>()
        val nestedRefs = mutableListOf<PersonRefNode>()
        val lists = mutableListOf<RelationListNode>()

        person.father?.takeIf { isComplete || it !== source }?.let { father ->
            appendExported(father, person, isComplete, exportedPersons, nestedPersons, nestedRefs) {
                it.copy(relation = Person.PROPERTY_FATHER)
            }
        }
        person.mother?.takeIf { isComplete || it !== source }?.let { mother ->
            appendExported(mother, person, isComplete, exportedPersons, nestedPersons, nestedRefs) {
                it.copy(relation = Person.PROPERTY_MOTHER)
            }
        }

        if (!isComplete && shouldInlineChildren(person, source)) {
            person.children.forEach { child ->
                if (child !== source) {
                    appendExported(child, person, isComplete, exportedPersons, nestedPersons, nestedRefs) {
                        it.copy(relation = IoConstants.P2PRELTYPE_CHILD)
                    }
                }
            }
        } else if (isComplete || !(person.children.isEmpty() ||
                person.children.size() == 1 && person.children.contains(source))
        ) {
            val listPersons = mutableListOf<PersonNode>()
            val listRefs = mutableListOf<PersonRefNode>()
            person.children.forEach { child ->
                if (child === source && !isComplete) return@forEach
                when (val exported = exportPerson(child, person, isComplete, exportedPersons)) {
                    is ExportedPerson.Full -> listPersons += exported.node
                    is ExportedPerson.Reference -> listRefs += exported.ref
                }
            }
            if (listPersons.isNotEmpty() || listRefs.isNotEmpty() || isComplete) {
                lists += RelationListNode(
                    of = IoConstants.P2PRELTYPE_CHILD,
                    persons = listPersons.takeIf { it.isNotEmpty() },
                    personRefs = listRefs.takeIf { it.isNotEmpty() },
                )
            }
        }

        return ExportedPerson.Full(
            node.copy(
                nestedPersons = nestedPersons.takeIf { it.isNotEmpty() },
                personRefs = nestedRefs.takeIf { it.isNotEmpty() },
                lists = lists.takeIf { it.isNotEmpty() },
            ),
        )
    }

    private fun appendExported(
        related: Person,
        source: Person,
        isComplete: Boolean,
        exportedPersons: MutableMap<Person, PersonNode>,
        nestedPersons: MutableList<PersonNode>,
        nestedRefs: MutableList<PersonRefNode>,
        withRelation: (PersonNode) -> PersonNode,
    ) {
        when (val exported = exportPerson(related, source, isComplete, exportedPersons)) {
            is ExportedPerson.Full -> nestedPersons += withRelation(exported.node)
            is ExportedPerson.Reference -> nestedRefs += exported.ref
        }
    }

    private fun ensureExportedId(
        existing: PersonNode,
        person: Person,
        exportedPersons: MutableMap<Person, PersonNode>,
    ) {
        if (existing.id == null && !person.id!!.startsWith(Person.GENERATED_ID_PREFIX)) {
            exportedPersons[person] = existing.copy(id = person.id)
        }
    }

    private fun personRefNode(ref: PersonRefNode): PersonNode {
        return PersonNode(personRefs = listOf(ref))
    }

    private fun shouldInlineChildren(person: Person, source: Person?): Boolean {
        val childCount = person.children.size()
        val containsSource = person.children.contains(source)
        return childCount > (if (containsSource) 2 else 1)
    }
}

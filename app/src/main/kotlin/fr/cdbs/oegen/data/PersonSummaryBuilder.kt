package fr.cdbs.oegen.data

import fr.cdbs.oegen.data.date.LifeEventKind

object PersonSummaryBuilder {
    data class MarriageGroup(
        val spouse: Person?,
        val children: List<Person>,
    )

    fun marriageGroups(person: Person): List<MarriageGroup> {
        val groups = linkedMapOf<Person?, MutableList<Person>>()
        for (child in person.children) {
            val spouse = when {
                child.father === person -> child.mother
                child.mother === person -> child.father
                else -> null
            }
            groups.getOrPut(spouse) { mutableListOf() }.add(child)
        }
        return groups.map { (spouse, children) -> MarriageGroup(spouse, children.sortedBy { it.lastName.orEmpty() }) }
    }

    fun ancestorGenerations(person: Person, maxGenerations: Int): List<List<Person>> {
        if (maxGenerations <= 0) return emptyList()
        val result = mutableListOf<List<Person>>()
        var current = listOf(person)
        repeat(maxGenerations) {
            val parents = current.flatMap { p ->
                listOfNotNull(p.father, p.mother)
            }.distinct()
            if (parents.isEmpty()) return result
            result += parents
            current = parents
        }
        return result
    }

    fun descendantGenerations(person: Person, maxGenerations: Int): List<List<Person>> {
        if (maxGenerations <= 0) return emptyList()
        val result = mutableListOf<List<Person>>()
        var current = listOf(person)
        repeat(maxGenerations) {
            val children = current.flatMap { p -> p.children.filterNotNull() }.distinct()
            if (children.isEmpty()) return result
            result += children
            current = children
        }
        return result
    }
}

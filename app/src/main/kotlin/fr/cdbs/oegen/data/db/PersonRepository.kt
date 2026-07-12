package fr.cdbs.oegen.data.db

import fr.cdbs.oegen.data.Person
import java.sql.Connection

class PersonRepository(private val connection: Connection) {

    fun loadAllRows(): List<PersonRow> {
        connection.createStatement().use { stmt ->
            stmt.executeQuery(
                """
                SELECT id, first_name, last_name, gender, father_id, mother_id, extra_json
                FROM persons
                ORDER BY id
                """.trimIndent(),
            ).use { rs ->
                val rows = mutableListOf<PersonRow>()
                while (rs.next()) {
                    rows.add(
                        PersonRow(
                            id = rs.getString("id"),
                            firstName = rs.getString("first_name"),
                            lastName = rs.getString("last_name"),
                            gender = rs.getString("gender"),
                            fatherId = rs.getString("father_id"),
                            motherId = rs.getString("mother_id"),
                            extraJson = rs.getString("extra_json"),
                        ),
                    )
                }
                return rows
            }
        }
    }

    fun clearAll() {
        connection.createStatement().use { it.executeUpdate("DELETE FROM persons") }
    }

    fun replaceAll(persons: Iterable<Person>) {
        clearAll()
        insertPersonsWithoutParents(persons)
        updateParentLinks(persons)
    }

    private fun insertPersonsWithoutParents(persons: Iterable<Person>) {
        connection.prepareStatement(
            """
            INSERT INTO persons (id, first_name, last_name, gender, father_id, mother_id, extra_json)
            VALUES (?, ?, ?, ?, NULL, NULL, NULL)
            """.trimIndent(),
        ).use { ps ->
            for (person in persons) {
                val id = person.id ?: continue
                ps.setString(1, id)
                ps.setString(2, person.firstName)
                ps.setString(3, person.lastName)
                ps.setString(4, person.gender?.name)
                ps.addBatch()
            }
            ps.executeBatch()
        }
    }

    private fun updateParentLinks(persons: Iterable<Person>) {
        connection.prepareStatement(
            """
            UPDATE persons SET father_id = ?, mother_id = ? WHERE id = ?
            """.trimIndent(),
        ).use { ps ->
            for (person in persons) {
                val id = person.id ?: continue
                ps.setString(1, person.father?.id)
                ps.setString(2, person.mother?.id)
                ps.setString(3, id)
                ps.addBatch()
            }
            ps.executeBatch()
        }
    }
}

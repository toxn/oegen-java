package fr.cdbs.oegen.data.db

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.toList
import java.sql.Connection

/**
 * Primary persistence: JDBC SQLite (default) or PostgreSQL.
 * Keeps [Person.persons] as the in-memory working set for the UI.
 */
object DatabaseService {
    private var config: DbConfig = DbConfig.fromEnvironment()
    private var connection: Connection? = null
    private var openForUrl: String? = null

    fun configure(config: DbConfig) {
        close()
        this.config = config
    }

    fun currentConfig(): DbConfig = config

    fun initialize(config: DbConfig = DbConfig.fromEnvironment()) {
        configure(config)
        MigrationRunner.migrate(openConnection())
    }

    fun loadIntoMemory() {
        withRepository { repo ->
            Person.clearAll()
            val rows = repo.loadAllRows()
            if (rows.isEmpty()) return@withRepository

            val byId = linkedMapOf<String, Person>()
            for (row in rows) {
                val person = Person()
                person.id = row.id
                person.firstName = row.firstName
                person.lastName = row.lastName
                person.gender = row.gender?.let { name ->
                    Person.Gender.entries.firstOrNull { g -> g.name == name }
                } ?: Person.Gender.Unknown
                person.lifeEvents = PersonExtraJson.decode(row.extraJson)
                byId[row.id] = person
            }
            for (row in rows) {
                val person = byId[row.id] ?: continue
                row.fatherId?.let { fatherId -> byId[fatherId]?.let { person.father = it } }
                row.motherId?.let { motherId -> byId[motherId]?.let { person.mother = it } }
            }
        }
    }

    fun saveFromMemory() {
        withRepository { repo ->
            repo.replaceAll(Person.persons.toList())
        }
    }

    fun clearDatabase() {
        withRepository { repo ->
            repo.clearAll()
        }
        Person.clearAll()
    }

    fun close() {
        connection?.close()
        connection = null
        openForUrl = null
    }

    private fun openConnection(): Connection {
        val existing = connection
        if (existing != null && !existing.isClosed && openForUrl == config.jdbcUrl) {
            return existing
        }
        existing?.close()
        val conn = DbConnection.open(config)
        connection = conn
        openForUrl = config.jdbcUrl
        return conn
    }

    private inline fun <T> withRepository(block: (PersonRepository) -> T): T {
        val conn = openConnection()
        return try {
            val result = block(PersonRepository(conn))
            conn.commit()
            result
        } catch (e: Exception) {
            conn.rollback()
            throw e
        }
    }
}

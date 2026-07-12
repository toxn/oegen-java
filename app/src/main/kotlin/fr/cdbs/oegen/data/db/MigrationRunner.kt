package fr.cdbs.oegen.data.db

import java.sql.Connection

object MigrationRunner {
    private val SCRIPTS = listOf(
        1 to "db/migration/V001__init.sql",
    )

    fun migrate(connection: Connection) {
        ensureVersionTable(connection)
        val current = readVersion(connection)
        for ((version, resourcePath) in SCRIPTS) {
            if (version <= current) continue
            val sql = MigrationRunner::class.java.classLoader.getResourceAsStream(resourcePath)?.bufferedReader()
                ?.use { it.readText() }
                ?: error("Missing migration resource: $resourcePath")
            connection.createStatement().use { stmt ->
                sql.split(';')
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .forEach { stmt.addBatch(it) }
                stmt.executeBatch()
            }
            writeVersion(connection, version)
            connection.commit()
        }
    }

    private fun ensureVersionTable(connection: Connection) {
        connection.createStatement().use { stmt ->
            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS schema_version (
                    version INTEGER NOT NULL
                )
                """.trimIndent(),
            )
        }
        connection.commit()
    }

    private fun readVersion(connection: Connection): Int {
        connection.createStatement().use { stmt ->
            stmt.executeQuery("SELECT version FROM schema_version ORDER BY version DESC LIMIT 1").use { rs ->
                return if (rs.next()) rs.getInt(1) else 0
            }
        }
    }

    private fun writeVersion(connection: Connection, version: Int) {
        connection.createStatement().use { stmt ->
            stmt.executeUpdate("DELETE FROM schema_version")
            stmt.executeUpdate("INSERT INTO schema_version (version) VALUES ($version)")
        }
    }
}

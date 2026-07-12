package fr.cdbs.oegen.data.db

import java.sql.Connection
import java.sql.DriverManager

object DbConnection {
    fun open(config: DbConfig): Connection {
        if (config.isSqlite()) {
            Class.forName("org.sqlite.JDBC")
        } else if (config.isPostgresql()) {
            Class.forName("org.postgresql.Driver")
        }
        return DriverManager.getConnection(config.jdbcUrl, config.connectionProperties()).apply {
            autoCommit = false
            if (config.isSqlite()) {
                createStatement().use { it.execute("PRAGMA foreign_keys = ON") }
            }
        }
    }
}

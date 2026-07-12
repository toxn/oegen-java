package fr.cdbs.oegen.data.db

import java.io.File
import java.util.Properties
import java.util.UUID

data class DbConfig(
    val jdbcUrl: String,
    val username: String?,
    val password: String?,
    val sqliteFile: File?,
) {
    val displayName: String
        get() = sqliteFile?.name ?: jdbcUrl.substringAfterLast('/').ifEmpty { jdbcUrl }

    companion object {
        private const val ENV_URL = "OEGEN_DB_URL"
        private const val ENV_USER = "OEGEN_DB_USER"
        private const val ENV_PASSWORD = "OEGEN_DB_PASSWORD"
        private const val ENV_PATH = "OEGEN_DB_PATH"
        private const val PROP_URL = "oegen.db.url"
        private const val PROP_PATH = "oegen.db.path"

        fun fromEnvironment(): DbConfig {
            val url = System.getenv(ENV_URL)?.takeIf { it.isNotBlank() }
                ?: System.getProperty(PROP_URL)?.takeIf { it.isNotBlank() }

            if (url != null) {
                return DbConfig(
                    jdbcUrl = url,
                    username = System.getenv(ENV_USER)?.takeIf { it.isNotBlank() },
                    password = System.getenv(ENV_PASSWORD)?.takeIf { it.isNotBlank() },
                    sqliteFile = null,
                )
            }

            val dbFile = resolveSqliteFile()
            dbFile.parentFile?.mkdirs()
            return DbConfig(
                jdbcUrl = "jdbc:sqlite:${dbFile.absolutePath}",
                username = null,
                password = null,
                sqliteFile = dbFile,
            )
        }

        /** Isolated in-memory database for unit tests (unique per call). */
        fun inMemorySqlite(): DbConfig {
            val id = UUID.randomUUID().toString().replace("-", "")
            return DbConfig(
                jdbcUrl = "jdbc:sqlite:file:oegen-test-$id?mode=memory&cache=shared",
                username = null,
                password = null,
                sqliteFile = null,
            )
        }

        private fun resolveSqliteFile(): File {
            System.getenv(ENV_PATH)?.takeIf { it.isNotBlank() }?.let { return File(it) }
            System.getProperty(PROP_PATH)?.takeIf { it.isNotBlank() }?.let { return File(it) }
            return File(System.getProperty("user.home"), ".oegen/oegen.db")
        }
    }

    fun connectionProperties(): Properties {
        val props = Properties()
        username?.let { props["user"] = it }
        password?.let { props["password"] = it }
        return props
    }

    fun isPostgresql(): Boolean = jdbcUrl.startsWith("jdbc:postgresql:", ignoreCase = true)

    fun isSqlite(): Boolean = jdbcUrl.startsWith("jdbc:sqlite:", ignoreCase = true)
}

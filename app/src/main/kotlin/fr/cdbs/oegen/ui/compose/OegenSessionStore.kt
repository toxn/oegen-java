package fr.cdbs.oegen.ui.compose

import java.io.File
import java.util.prefs.Preferences

/**
 * Persists the last opened database path (Java Preferences with ~/.oegen/ fallback).
 */
object OegenSessionStore {
    private const val PREFS_NODE = "fr/cdbs/oegen"
    private const val PREFS_KEY_LAST_DATABASE = "lastDatabasePath"

    internal var preferences: Preferences = Preferences.userRoot().node(PREFS_NODE)

    private fun legacyPathFile(): File =
        File(System.getProperty("user.home"), ".oegen/last-database.path")

    fun getLastDatabasePath(): File? {
        val path = preferences.get(PREFS_KEY_LAST_DATABASE, null)?.takeIf { it.isNotBlank() }
            ?: legacyPathFile().takeIf { it.isFile }?.readText()?.trim()?.takeIf { it.isNotEmpty() }
        return path?.let { File(it) }?.takeIf { it.isFile && it.canRead() }
    }

    fun setLastDatabasePath(file: File?) {
        if (file == null) {
            preferences.remove(PREFS_KEY_LAST_DATABASE)
        } else {
            preferences.put(PREFS_KEY_LAST_DATABASE, file.absolutePath)
        }
        preferences.flush()

        val legacy = legacyPathFile()
        if (file == null) {
            legacy.delete()
        } else {
            legacy.parentFile?.mkdirs()
            legacy.writeText(file.absolutePath)
        }
    }
}

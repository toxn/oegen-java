package fr.cdbs.oegen.ui.compose

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.prefs.Preferences

class OegenSessionStoreTest {
    @TempDir
    lateinit var tempDir: File

    @AfterEach
    fun tearDown() {
        OegenSessionStore.preferences.remove("lastDatabasePath")
        OegenSessionStore.preferences.flush()
    }

    @Test
    fun roundTripsLastDatabasePathViaPreferences() {
        val prefs = Preferences.userRoot().node("fr/cdbs/oegen/test-${System.nanoTime()}")
        OegenSessionStore.preferences = prefs

        val db = File(tempDir, "family.xml").apply { writeText("<oegen-db/>") }
        OegenSessionStore.setLastDatabasePath(db)

        assertEquals(db, OegenSessionStore.getLastDatabasePath())
    }

    @Test
    fun returnsNullWhenFileMissing() {
        val prefs = Preferences.userRoot().node("fr/cdbs/oegen/test-${System.nanoTime()}")
        OegenSessionStore.preferences = prefs

        prefs.put("lastDatabasePath", File(tempDir, "gone.xml").absolutePath)
        prefs.flush()

        assertNull(OegenSessionStore.getLastDatabasePath())
    }
}

package fr.cdbs.oegen.ui.compose

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.db.DatabaseService
import fr.cdbs.oegen.data.io.xml.Exporter
import fr.cdbs.oegen.data.io.xml.Importer
import fr.cdbs.oegen.ui.toList
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

object DatabaseActions {
    private val fileChooser = JFileChooser().apply {
        fileFilter = FileNameExtensionFilter("Œgen XML (*.xml)", "xml")
    }

    fun open(state: OegenAppState): Boolean {
        if (state.isDirty && !confirmDiscardChanges(state)) return false

        if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return false

        Person.clearAll()
        FileInputStream(fileChooser.selectedFile).use { Importer(it).doImport() }
        DatabaseService.saveFromMemory()
        state.saveFile = fileChooser.selectedFile
        state.markSaved()
        state.selectPerson(Person.persons.toList().firstOrNull())
        return true
    }

    fun save(state: OegenAppState): Boolean {
        DatabaseService.saveFromMemory()
        state.markSaved(state.saveFile)
        return true
    }

    fun saveAs(state: OegenAppState): Boolean {
        if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return false

        val target = fileChooser.selectedFile
        exportToXml(target)
        state.saveFile = target
        state.markSaved(target)
        return true
    }

    fun newDatabase(state: OegenAppState): Boolean {
        if (state.isDirty && !confirmDiscardChanges(state)) return false

        DatabaseService.clearDatabase()
        state.saveFile = null
        state.isDirty = false
        state.selectPerson(null)
        return true
    }

    fun confirmDiscardChanges(state: OegenAppState): Boolean {
        val options = arrayOf(
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DialogOption.Continue"),
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DialogOption.SaveFirst"),
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DialogOption.Cancel"),
        )
        val result = JOptionPane.showOptionDialog(
            null,
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DataLostWarning.Message"),
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DataLostWarning.Title"),
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[2],
        )
        return when (result) {
            0 -> true
            1 -> save(state)
            else -> false
        }
    }

    fun confirmExit(state: OegenAppState): Boolean {
        if (!state.isDirty) return true

        val options = arrayOf(
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DialogOption.Continue"),
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DialogOption.SaveFirst"),
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DialogOption.Cancel"),
        )
        val result = JOptionPane.showOptionDialog(
            null,
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DataLostWarning.Message"),
            fr.cdbs.oegen.ui.Messages.getString("MainWindowSwing.DataLostWarning.Title"),
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[2],
        )
        return when (result) {
            JOptionPane.CLOSED_OPTION, 2 -> false
            0 -> true
            1 -> save(state)
            else -> false
        }
    }

    fun exportToXml(file: File) {
        FileOutputStream(file).use { Exporter(it).doExport() }
    }
}

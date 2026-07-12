package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.Messages
import fr.cdbs.oegen.ui.searchByName
import java.awt.Component
import javax.swing.JList
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.ListSelectionModel

object PersonSearch {
    fun showAndSelect(parent: Component): Person? {
        val queryField = JTextField(24)
        val prompt = JOptionPane.showConfirmDialog(
            parent,
            queryField,
            Messages.getString("Person.Search.Prompt"),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
        )
        if (prompt != JOptionPane.OK_OPTION) return null

        val foundPersons = Person.persons.searchByName(queryField.text)
        if (foundPersons.isEmpty()) {
            JOptionPane.showMessageDialog(
                parent,
                Messages.getString("Person.Search.NoMatch"),
                Messages.getString("Person.Search.Title"),
                JOptionPane.INFORMATION_MESSAGE,
            )
            return null
        }

        if (foundPersons.size == 1) return foundPersons[0]

        val list = JList(foundPersons.map { it.toString() }.toTypedArray())
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.selectedIndex = 0

        val result = JOptionPane.showConfirmDialog(
            parent,
            JScrollPane(list),
            Messages.getString("Person.Search.Select"),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
        )
        val selectedIndex = list.selectedIndex
        if (result != JOptionPane.OK_OPTION || selectedIndex < 0) return null

        return foundPersons[selectedIndex]
    }
}

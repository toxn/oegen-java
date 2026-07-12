package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.Person.Gender
import fr.cdbs.oegen.ui.Messages
import fr.cdbs.oegen.ui.PersonEditor as PersonEditorInterface
import fr.cdbs.oegen.ui.PersonList
import fr.cdbs.oegen.ui.PersonTableModel
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing.Box
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
import javax.swing.JTable
import javax.swing.JTextField

class PersonEditor :
    JPanel(GridBagLayout()),
    PersonEditorInterface,
    PropertyChangeListener,
    ActionListener,
    FocusListener {

    private var editedPerson: Person? = null

    private val firstNameTextField = JTextField(20)
    private val lastNameTextField = JTextField(20)
    private val genderComboBox = JComboBox(Gender.entries.toTypedArray())
    private val childrenTable: JTable
    private val subsTab: JTabbedPane

    init {
        var gbc = GridBagConstraints()
        gbc.anchor = GridBagConstraints.EAST
        add(JLabel(Messages.getString("Person.firstName") + " "), gbc)

        gbc = GridBagConstraints()
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.weightx = 1.0
        gbc.anchor = GridBagConstraints.BASELINE_LEADING
        add(firstNameTextField, gbc)

        gbc = GridBagConstraints()
        gbc.anchor = GridBagConstraints.EAST
        add(JLabel(Messages.getString("Person.lastName") + " "), gbc)

        gbc = GridBagConstraints()
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.weightx = 1.0
        gbc.anchor = GridBagConstraints.BASELINE_LEADING
        add(lastNameTextField, gbc)

        gbc = GridBagConstraints()
        gbc.anchor = GridBagConstraints.EAST
        add(JLabel(Messages.getString("Person.Gender") + " "), gbc)

        gbc = GridBagConstraints()
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.anchor = GridBagConstraints.BASELINE_LEADING
        add(genderComboBox, gbc)

        gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.BOTH
        gbc.gridwidth = GridBagConstraints.REMAINDER
        gbc.gridheight = GridBagConstraints.REMAINDER
        gbc.weightx = 1.0
        gbc.weighty = 1.0
        subsTab = JTabbedPane()
        add(subsTab, gbc)

        val childrenBox = Box.createVerticalBox()
        val childrenBoxButtonBar = Box.createHorizontalBox()
        childrenBox.add(childrenBoxButtonBar)

        childrenTable = JTable()
        childrenBox.add(JScrollPane(childrenTable))

        subsTab.add(Messages.getString("Person.children"), childrenBox)

        lastNameTextField.setActionCommand(ACTIONCOMMAND_CHANGE_LASTNAME)
        lastNameTextField.addActionListener(this)
        lastNameTextField.addFocusListener(this)
        firstNameTextField.setActionCommand(ACTIONCOMMAND_CHANGE_FIRSTNAME)
        firstNameTextField.addActionListener(this)
        firstNameTextField.addFocusListener(this)
        genderComboBox.setActionCommand(ACTIONCOMMAND_CHANGE_GENDER)
        genderComboBox.addActionListener(this)
        genderComboBox.addFocusListener(this)
    }

    override fun actionPerformed(e: ActionEvent) {
        when (e.actionCommand) {
            ACTIONCOMMAND_CHANGE_FIRSTNAME -> copyFirstNameToPerson()
            ACTIONCOMMAND_CHANGE_LASTNAME -> copyLastNameToPerson()
            ACTIONCOMMAND_CHANGE_GENDER -> copyGenderToPerson()
        }
    }

    private fun copyFirstNameFromPerson() {
        editedPerson ?: return
        firstNameTextField.text = editedPerson!!.firstName
    }

    private fun copyFirstNameToPerson() {
        editedPerson ?: return
        editedPerson!!.firstName = firstNameTextField.text
    }

    private fun copyGenderFromPerson() {
        editedPerson ?: return
        genderComboBox.selectedItem = editedPerson!!.gender
    }

    private fun copyGenderToPerson() {
        editedPerson ?: return
        editedPerson!!.gender = genderComboBox.selectedItem as Gender?
    }

    private fun copyLastNameFromPerson() {
        editedPerson ?: return
        lastNameTextField.text = editedPerson!!.lastName
    }

    private fun copyLastNameToPerson() {
        editedPerson ?: return
        editedPerson!!.lastName = lastNameTextField.text
    }

    override fun focusGained(fe: FocusEvent) {
        // Nothing treated here
    }

    override fun focusLost(fe: FocusEvent) {
        when (fe.component) {
            firstNameTextField -> copyFirstNameToPerson()
            lastNameTextField -> copyLastNameToPerson()
            genderComboBox -> copyGenderToPerson()
        }
    }

    override var person: Person?
        get() = editedPerson
        set(newValue) {
            if (newValue === editedPerson) return

            editedPerson?.removePropertyChangeListener(this)
            editedPerson = newValue

            if (editedPerson == null) {
                isEnabled = false
                firstNameTextField.text = null
                lastNameTextField.text = null
                genderComboBox.selectedItem = Gender.Unknown
                childrenTable.clearSelection()
                childrenTable.model = PersonTableModel(PersonList())
            } else {
                isEnabled = true
                copyFirstNameFromPerson()
                copyLastNameFromPerson()
                copyGenderFromPerson()
                childrenTable.model = PersonTableModel(editedPerson!!.children)
                editedPerson!!.addPropertyChangeListener(this)
            }
        }

    override fun propertyChange(evt: PropertyChangeEvent) {
        if (evt.source != editedPerson) return

        when (evt.propertyName) {
            Person.PROPERTY_FIRSTNAME -> copyFirstNameFromPerson()
            Person.PROPERTY_LASTNAME -> copyLastNameFromPerson()
            Person.PROPERTY_GENDER -> copyGenderFromPerson()
        }
    }

    companion object {
        private const val ACTIONCOMMAND_CHANGE_LASTNAME = "copy lastName to person"
        private const val ACTIONCOMMAND_CHANGE_FIRSTNAME = "copy fistName to person"
        private const val ACTIONCOMMAND_CHANGE_GENDER = "copy gender to person"
    }
}

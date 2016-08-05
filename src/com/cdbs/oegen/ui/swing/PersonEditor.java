/**
 *
 */
package com.cdbs.oegen.ui.swing;

import com.cdbs.oegen.data.Person;
import com.cdbs.oegen.data.Person.Gender;
import com.cdbs.oegen.ui.Messages;
import com.cdbs.oegen.ui.PersonList;
import com.cdbs.oegen.ui.PersonTableModel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * @author toxn
 *
 */
public class PersonEditor extends JPanel
implements com.cdbs.oegen.ui.PersonEditor, PropertyChangeListener, ActionListener, FocusListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final String ACTIONCOMMAND_CHANGE_LASTNAME = "copy lastName to person"; //$NON-NLS-1$

    private static final String ACTIONCOMMAND_CHANGE_FIRSTNAME = "copy fistName to person"; //$NON-NLS-1$

    private static final String ACTIONCOMMAND_CHANGE_GENDER = "copy gender to person"; //$NON-NLS-1$

    private Person person;

    private final JTextField firstNameTextField = new JTextField(20);
    private final JTextField lastNameTextField = new JTextField(20);
    private final JComboBox<Gender> genderComboBox = new JComboBox<>(Gender.values());

    private final JTable childrenTable;

    private final JTabbedPane subsTab;

    PersonEditor() {
	super(new GridBagLayout());

	GridBagConstraints gbc;

	gbc = new GridBagConstraints();
	gbc.anchor = GridBagConstraints.EAST;
	add(new JLabel(Messages.getString("Person.firstName") + " "), gbc); //$NON-NLS-1$ //$NON-NLS-2$

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.weightx = 1.0;
	gbc.anchor = GridBagConstraints.BASELINE_LEADING;
	add(firstNameTextField, gbc);

	gbc = new GridBagConstraints();
	gbc.anchor = GridBagConstraints.EAST;
	add(new JLabel(Messages.getString("Person.lastName") + " "), gbc); //$NON-NLS-1$ //$NON-NLS-2$

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.weightx = 1.0;
	gbc.anchor = GridBagConstraints.BASELINE_LEADING;
	add(lastNameTextField, gbc);

	gbc = new GridBagConstraints();
	gbc.anchor = GridBagConstraints.EAST;
	add(new JLabel(Messages.getString("Person.Gender") + " "), gbc); //$NON-NLS-1$ //$NON-NLS-2$

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.anchor = GridBagConstraints.BASELINE_LEADING;
	add(genderComboBox, gbc);

	gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.gridheight = GridBagConstraints.REMAINDER;
	gbc.weightx = 1.0;
	gbc.weighty = 1.0;
	subsTab = new JTabbedPane();
	add(subsTab, gbc);

	Box childrenBox = Box.createVerticalBox();
	Box childrenBoxButtonBar = Box.createHorizontalBox();

	// TODO: add buttons

	childrenBox.add(childrenBoxButtonBar);

	childrenTable = new JTable();
	childrenBox.add(new JScrollPane(childrenTable));

	subsTab.add(Messages.getString("Person.children"), childrenBox); //$NON-NLS-1$

	lastNameTextField.setActionCommand(ACTIONCOMMAND_CHANGE_LASTNAME);
	lastNameTextField.addActionListener(this);
	lastNameTextField.addFocusListener(this);
	firstNameTextField.setActionCommand(ACTIONCOMMAND_CHANGE_FIRSTNAME);
	firstNameTextField.addActionListener(this);
	firstNameTextField.addFocusListener(this);
	genderComboBox.setActionCommand(ACTIONCOMMAND_CHANGE_GENDER);
	genderComboBox.addActionListener(this);
	genderComboBox.addFocusListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	switch (e.getActionCommand()) {
	case ACTIONCOMMAND_CHANGE_FIRSTNAME:
	    copyFirstNameToPerson();
	    return;

	case ACTIONCOMMAND_CHANGE_LASTNAME:
	    copyLastNameToPerson();
	    return;

	case ACTIONCOMMAND_CHANGE_GENDER:
	    copyGenderToPerson();
	    return;
	}

    }

    private void copyFirstNameFromPerson() {
	if (person == null)
	    return;

	firstNameTextField.setText(person.getFirstName());
    }

    private void copyFirstNameToPerson() {
	if (person == null)
	    return;

	person.setFirstName(firstNameTextField.getText());
    }

    private void copyGenderFromPerson() {
	if (person == null)
	    return;

	genderComboBox.setSelectedItem(person.getGender());
    }

    private void copyGenderToPerson() {
	if (person == null)
	    return;

	person.setGender((Gender) genderComboBox.getSelectedItem());
    }

    private void copyLastNameFromPerson() {
	if (person == null)
	    return;

	lastNameTextField.setText(person.getLastName());
    }

    private void copyLastNameToPerson() {
	if (person == null)
	    return;

	person.setLastName(lastNameTextField.getText());
    }

    @Override
    public void focusGained(FocusEvent fe) {
	// Nothing treated here

    }

    @Override
    public void focusLost(FocusEvent fe) {
	if(fe.getComponent()==firstNameTextField) {
	    copyFirstNameToPerson();
	} else if(fe.getComponent() == lastNameTextField) {
	    copyLastNameToPerson();
	} else if(fe.getComponent()==genderComboBox) {
	    copyGenderToPerson();
	}
    }

    @Override
    public Person getPerson() {
	return person;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getSource() != person)
	    return;

	switch (evt.getPropertyName()) {
	case Person.PROPERTY_FIRSTNAME:
	    copyFirstNameFromPerson();
	    return;

	case Person.PROPERTY_LASTNAME:
	    copyLastNameFromPerson();
	    return;

	case Person.PROPERTY_GENDER:
	    copyGenderFromPerson();
	    return;

	default:
	}

    }

    public void setPerson(Person newValue) {
	if (newValue == person)
	    return;

	if (person != null) {
	    person.removePropertyChangeListener(this);
	}
	person = newValue;

	if (person == null) {
	    setEnabled(false);
	    firstNameTextField.setText(null);
	    lastNameTextField.setText(null);
	    genderComboBox.setSelectedItem(Gender.Unknown);

	    childrenTable.clearSelection();
        childrenTable.setModel(new PersonTableModel(new PersonList()));

	} else {
	    setEnabled(true);

	    // Update displayed fields
	    copyFirstNameFromPerson();
	    copyLastNameFromPerson();
	    copyGenderFromPerson();

	    childrenTable.setModel(new PersonTableModel(person.children));

	    // subscribe this to changes of person's field.
	    person.addPropertyChangeListener(this);
	}
    }
}

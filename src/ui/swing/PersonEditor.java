/**
 *
 */
package ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.Person;
import ui.Messages;

/**
 * @author toxn
 *
 */
public class PersonEditor extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Person person;

    PersonEditor() {
	super(new GridBagLayout());

	GridBagConstraints gbc;

	add(new JLabel(Messages.getString("Person.firstName"))); //$NON-NLS-1$

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	add(new JTextField(), gbc);

	add(new JLabel(Messages.getString("Person.lastName"))); //$NON-NLS-1$

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	add(new JTextField(), gbc);

	add(new JComboBox<Person.Gender>(Person.Gender.values()));

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	add(new JPanel(), gbc);

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

	gbc = new GridBagConstraints();
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gbc.gridheight = GridBagConstraints.REMAINDER;
	add(new JTabbedPane(), gbc);
    }

    public void setPerson(Person newPerson) {
	if (newPerson == person)
	    return;

	person = newPerson;
    }
}

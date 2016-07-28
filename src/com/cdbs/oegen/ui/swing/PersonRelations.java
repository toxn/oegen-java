/**
 *
 */
package com.cdbs.oegen.ui.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;

import com.cdbs.oegen.data.Person;

/**
 * @author toxn
 *
 */
public class PersonRelations extends PersonRelationalComponent {
    /**
     * @author toxn
     *
     */
    public class PersonRelationsDocument extends DefaultStyledDocument implements PropertyChangeListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 9111073398636122377L;

	private final Person person;

	/**
	 *
	 * @param person
	 *            must not be null
	 */
	public PersonRelationsDocument(Person person) {
	    super();

	    if (person == null)
		throw new NullPointerException();

	    this.person = person;
	    person.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    switch (evt.getPropertyName()) {
	    case Person.PROPERTY_FIRSTNAME:
	    case Person.PROPERTY_LASTNAME:
	    case Person.PROPERTY_GENDER:
	    case Person.PROPERTY_FATHER:
	    case Person.PROPERTY_MOTHER:
	    case Person.PROPERTY_CHILDREN:
	    }

	}

    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final JTextPane textPane = new JTextPane();

    public PersonRelations() {
	this.add(textPane);
    }

    @Override
    protected void rebuild() {
	textPane.setStyledDocument(null);

	Person center = getCenter();

	if(center == null) return;

	textPane.setStyledDocument(new PersonRelationsDocument(center));
    }

}

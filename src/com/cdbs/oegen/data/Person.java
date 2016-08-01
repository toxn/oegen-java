/**
 *
 */
package com.cdbs.oegen.data;

import com.cdbs.oegen.ui.Messages;
import com.cdbs.oegen.ui.PersonList;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.event.ChangeEvent;

/**
 * @author toxn
 *
 */
public class Person
{
    public PersonList getSiblings() {
        if(siblings == null)
            siblings = new SiblingList(this);
        
        return siblings;
    }
    public enum Gender {
        Unknown(Messages.getString("Person.Gender.Unknown"), "?"), //$NON-NLS-2$
        Male(Messages.getString("Person.Gender.Male"), "\u2642"), //$NON-NLS-2$
        Female(Messages.getString("Person.Gender.Female"), "\u2640"), //$NON-NLS-2$
        Other(Messages.getString("Person.Gender.Other"), "\u26A7"); //$NON-NLS-2$

        private final String symbol;
        private final String nlsName;

        Gender(String nlsName, String symbol) {
            this.nlsName = nlsName;
            this.symbol = symbol;
        }

        public final String getSymbol() {
            return symbol;
        }

        public final String getNlsName() {
            return nlsName;
        }

        @Override
        public String toString() {
            return nlsName;
        }
    }
    public static final String GENERATED_ID_PREFIX = "$"; //$NON-NLS-1$

    private SiblingList siblings = null;
    
    public static PersonList persons = new PersonList();

    public static HashMap<String, Person> indexId = new HashMap<>();
    public static final String PROPERTY_FATHER = "father"; //$NON-NLS-1$

    public static final String PROPERTY_FIRSTNAME = "firstName"; //$NON-NLS-1$

    public static final String PROPERTY_GENDER = "gender"; //$NON-NLS-1$

    public static final String PROPERTY_LASTNAME = "lastName"; //$NON-NLS-1$

    public static final String PROPERTY_MOTHER = "mother"; //$NON-NLS-1$

    public static final String PROPERTY_ID = "id"; //$NON-NLS-1$

    public static final String CLASSNAME = "person"; //$NON-NLS-1$

    public static final String PROPERTY_CHILDREN = "children"; //$NON-NLS-1$

    public static final String TAG_CHILD = "child"; //$NON-NLS-1$

    public final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public PersonList children = new PersonList();
    private Gender gender = Gender.Unknown;

    private String firstName = ""; //$NON-NLS-1$

    private String lastName = ""; //$NON-NLS-1$

    private Person father;

    private Person mother;

    private String id;

    public Person() {
	super();
	persons.addElement(this);
	setId(null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
	pcs.addPropertyChangeListener(listener);
    }

    /**
     * @return the father
     */
    public Person getFather()
    {
	return father;
    }

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
	return firstName;
    }

    /**
     * @return le gender
     */
    public Gender getGender()
    {
	return gender;
    }

    /**
     * @return le id
     */
    public String getId() {
	return id;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
	return lastName;
    }

    /**
     * @return the mother
     */
    public Person getMother()
    {
	return mother;
    }

    public void Remove()
    {
	persons.removeElement(this);

	// dereference without triggering stateChanged in persons
	firstName = null;
	lastName = null;
	father = null;
	mother = null;
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
	pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newValue
     *            the father to set
     */
    public void setFather(Person newValue)
    {
	if (father == newValue)
	    return;

	if (father != null) {
	    father.children.removeElement(this);
	}

	Person oldValue = father;

	father = newValue;

	pcs.firePropertyChange(PROPERTY_FATHER, oldValue, newValue);

	father.children.addElement(this);

	father.children.stateChanged(new ChangeEvent(this));
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param newValue the firstName to set
     */
    public void setFirstName(String newValue)
    {
	String oldValue = firstName;
	firstName = newValue;
	pcs.firePropertyChange(PROPERTY_FIRSTNAME, oldValue, newValue);
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param newValue
     *            le gender à définir
     */
    public void setGender(Gender newValue)
    {
	Gender oldValue = gender;
	gender = newValue;

	pcs.firePropertyChange(PROPERTY_GENDER, oldValue, newValue);

	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param newValue le id à définir
     */
    public void setId(String newValue) {
	String newId = newValue;
	if (newId == null) {
	    newId = GENERATED_ID_PREFIX + UUID.randomUUID().toString();
	}

	if (id != null) {
	    indexId.remove(id);
	}

	String oldId = id;

	id = newId;
	indexId.put(newId, this);

	pcs.firePropertyChange(PROPERTY_ID, oldId, newId);
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param newValue
     *            the lastName to set
     */
    public void setLastName(String newValue)
    {
	String oldValue = lastName;
	lastName = newValue;
	pcs.firePropertyChange(PROPERTY_LASTNAME, oldValue, newValue);
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param newValue
     *            the mother to set
     */
    public void setMother(Person newValue) {
	if (mother == newValue)
	    return;

	if (mother != null) {
	    mother.children.removeElement(this);
	}

	Person oldValue = mother;
	mother = newValue;
	pcs.firePropertyChange(PROPERTY_MOTHER, oldValue, newValue);

	mother.children.addElement(this);

	mother.children.stateChanged(new ChangeEvent(this));
	persons.stateChanged(new ChangeEvent(this));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	return firstName + " " + lastName; //$NON-NLS-1$
    }
}

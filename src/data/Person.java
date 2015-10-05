/**
 *
 */
package data;

import javax.swing.event.ChangeEvent;

import ui.PersonListModel;

/**
 * @author toxn
 *
 */
public class Person
{
    public enum Gender {
	Unknown,
	Male,
	Female,
	Other
    }

    public static PersonListModel persons = new PersonListModel();
    public PersonListModel children = new PersonListModel();

    private Gender gender = Gender.Unknown;

    private String firstName = "";

    private String lastName = "";

    private Person father;

    private Person mother;

    public Person() {
	super();
	persons.addElement(this);
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

    /**
     * @param father the father to set
     */
    public void setFather(Person father)
    {
	if (this.father == father)
	    return;

	if (this.father != null) {
	    this.father.children.removeElement(this);
	}

	this.father = father;

	this.father.children.addElement(this);

	this.father.children.stateChanged(new ChangeEvent(this));
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName)
    {
	this.firstName = firstName;
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param gender le gender à définir
     */
    public void setGender(Gender gender)
    {
	this.gender = gender;
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName)
    {
	this.lastName = lastName;
	persons.stateChanged(new ChangeEvent(this));
    }

    /**
     * @param mother the mother to set
     */
    public void setMother(Person mother)
    {
	if (this.mother == mother)
	    return;

	if (this.mother != null) {
	    this.mother.children.removeElement(this);
	}

	this.mother = mother;

	this.mother.children.addElement(this);

	this.mother.children.stateChanged(new ChangeEvent(this));
	persons.stateChanged(new ChangeEvent(this));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	return firstName + " " + lastName;
    }
}

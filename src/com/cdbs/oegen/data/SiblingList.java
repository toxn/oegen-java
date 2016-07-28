/**
 *
 */
package com.cdbs.oegen.data;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.cdbs.oegen.ui.PersonListModel;

/**
 * @author toxn
 *
 */
public class SiblingList extends PersonListModel implements ListDataListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Person person;

    public SiblingList(Person person) {
	this.person = person;

	Person father = person.getFather();
	Person mother = person.getMother();

	if (father != null) {
	    father.children.addListDataListener(this);
	    for (Person p : father.children)
		if (!contains(p)) {
		    addElement(p);
		}
	}

	if (mother != null) {
	    mother.children.addListDataListener(this);
	    for (Person p : mother.children)
		if (!contains(p)) {
		    addElement(p);
		}
	}

	removeElement(person);
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
	// Forward event to listeners
	fireContentsChanged(e, getSize(), getSize() - 1);
    }

    @Override
    protected void finalize() {
	try {
	    Person father = person.getFather();
	    Person mother = person.getMother();

	    if(father != null) {
		father.children.removeListDataListener(this);
	    }

	    if(mother != null) {
		mother.children.removeListDataListener(this);
	    }
	} finally {
	    try {
		super.finalize();
	    } catch (Throwable e) {
		// TODO Bloc catch généré automatiquement
		e.printStackTrace();
	    }
	}
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
	PersonListModel source = (PersonListModel) e.getSource();

	for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
	    Person p = source.elementAt(i);

	    if (!contains(p) && p != person) {
		addElement(p);
	    }
	}

    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
	PersonListModel source = (PersonListModel) e.getSource();

	PersonListModel motherChildren;
	PersonListModel fatherChildren;

	if (person.getMother() == null) {
	    motherChildren = null;
	} else {
	    motherChildren = person.getMother().children;
	}

	if (person.getFather() == null) {
	    fatherChildren = null;
	} else {
	    fatherChildren = person.getFather().children;
	}

	for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
	    Person p = source.elementAt(i);

	    if (contains(p)) {
		/*
		 * Element can be removed if there is only one parent, or if the
		 * sibling to remove was only a child of one parent.
		 */
		if (motherChildren == null || fatherChildren == null
			|| source == fatherChildren && !motherChildren.contains(p)
			|| source == motherChildren && !fatherChildren.contains(p)) {
		    removeElement(p);
		}
	    }
	}
    }
}

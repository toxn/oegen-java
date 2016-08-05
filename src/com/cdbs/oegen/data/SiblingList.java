/**
 *
 */
package com.cdbs.oegen.data;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.cdbs.oegen.ui.PersonList;

/**
 * @author toxn
 *
 */
public class SiblingList extends PersonList implements ListDataListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Person person;

    public SiblingList(Person person) {
	this.person = person;

	rebuildList();
    }

    @Override
    public void contentsChanged(ListDataEvent e) {

	// Forward event to listeners
	fireContentsChanged(e, getSize(), getSize() - 1);
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
	PersonList source = (PersonList) e.getSource();

	for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
	    Person p = source.elementAt(i);

	    if (!contains(p) && p != person) {
		addElement(p);
	    }
	}

    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
	Person father = person.getFather();
	Person mother = person.getMother();

	if (father != null) {
	    father.children.removeListDataListener(this);
	}

	if (mother != null) {
	    mother.children.removeListDataListener(this);
	}

	clear();
	fireIntervalRemoved(this, 0, getSize() - 1);

	rebuildList();
    }

    private
    void rebuildList() {
	Person father = person.getFather();
	Person mother = person.getMother();

	if (father != null) {
	    father.children.addListDataListener(this);
	    for (Person p : father.children)
		if (!contains(p) && p!=person) {
		    addElement(p);
		}
	}

	if (mother != null) {
	    mother.children.addListDataListener(this);
	    for (Person p : mother.children)
		if (!contains(p) && p!=person) {
		    addElement(p);
		}
	}

	fireIntervalAdded(this, 0, getSize() - 1);
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

}

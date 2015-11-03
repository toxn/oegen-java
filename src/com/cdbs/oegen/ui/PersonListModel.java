/**
 *
 */
package com.cdbs.oegen.ui;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.cdbs.oegen.data.Person;

/**
 * @author toxn
 *
 */
public final class PersonListModel
extends DefaultListModel<Person>
implements ChangeListener, Iterable<Person> {
    /**
     * @author toxn
     *
     */
    public class PersonListIterator implements Iterator<Person> {
	Enumeration<Person> enumeration = elements();

	/*
	 * !CodeTemplates.overridecomment.nonjd!
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
	    return enumeration.hasMoreElements();
	}

	/*
	 * !CodeTemplates.overridecomment.nonjd!
	 * 
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Person next() {
	    return enumeration.nextElement();
	}

    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public Iterator<Person> iterator() {
	return new PersonListIterator();
    }

    @Override
    public void stateChanged(ChangeEvent arg0)
    {
	// Get the index of the object triggering the event
	int changedIndex = this.indexOf(arg0.getSource());

	// and forward the change to all the listeners
	for (ListDataListener ldl : listenerList.getListeners(ListDataListener.class))
	{
	    ldl.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, changedIndex, changedIndex));
	}
    }
}

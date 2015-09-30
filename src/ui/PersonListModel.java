/**
 *
 */
package ui;

import javax.swing.DefaultListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import data.Person;

/**
 * @author toxn
 *
 */
public final class PersonListModel
extends DefaultListModel<Person>
implements ChangeListener
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

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

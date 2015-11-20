/**
 *
 */
package com.cdbs.oegen.ui;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.cdbs.oegen.data.Person;

/**
 * @author toxn
 *
 */
public class PersonTableModel implements TableModel, ListDataListener {
    /**
     * The list containing the persons to handle
     */
    private final PersonListModel persons;
    private final Set<TableModelListener> tableModelListeners = new HashSet<TableModelListener>();

    /**
     *
     */
    public PersonTableModel(PersonListModel personList) {
	persons = personList;
	persons.addListDataListener(this);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
	tableModelListeners.add(l);

    }

    @Override
    public void contentsChanged(ListDataEvent arg0) {
	TableModelEvent tme = new TableModelEvent(this, arg0.getIndex0(), arg0.getIndex1(), TableModelEvent.ALL_COLUMNS);

	for (TableModelListener tml : tableModelListeners) {
	    tml.tableChanged(tme);
	}
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
	switch(columnIndex) {
	case 0:
	    // Lastname
	    return String.class;

	case 1:
	    // Firstname
	    return String.class;

	case 2:
	    // Id
	    return String.class;

	default:
	    throw new RuntimeException(Messages.getString("PersonTableModel.InvalidColumnIndexException")); //$NON-NLS-1$
	}
    }

    /* !CodeTemplates.overridecomment.nonjd!
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
	// Columns are Last Name, First name.
	return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
	switch (columnIndex) {
	case 0:
	    // Lastname
	    return Messages.getString("Person.lastName"); //$NON-NLS-1$

	case 1:
	    // Firstname
	    return Messages.getString("Person.firstName"); //$NON-NLS-1$

	case 2:
	    // Id
	    return Messages.getString("Oebject.Id"); //$NON-NLS-1$

	default:
	    throw new RuntimeException(Messages.getString("PersonTableModel.InvalidColumnIndexException")); //$NON-NLS-1$
	}
    }

    public Person getPersonAt(int rowIndex) {
	if (rowIndex < 0 || rowIndex >= persons.size())
	    throw new RuntimeException(Messages.getString("PersonTableModel.InvalidRowIndexException")); //$NON-NLS-1$

	return persons.get(rowIndex);

    }

    /*
     * !CodeTemplates.overridecomment.nonjd!
     *
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
	return persons.size();
    }

    /*
     * !CodeTemplates.overridecomment.nonjd!
     *
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
	if (rowIndex < 0 || rowIndex >= persons.size())
	    throw new RuntimeException(Messages.getString("PersonTableModel.InvalidRowIndexException")); //$NON-NLS-1$

	Person person = persons.get(rowIndex);

	switch (colIndex) {
	case 0:
	    return person.getLastName();

	case 1:
	    return person.getFirstName();

	case 2:
	    return person.getId();
	}

	throw new RuntimeException(Messages.getString("PersonTableModel.InvalidColumnIndexException")); //$NON-NLS-1$
    }

    @Override
    public void intervalAdded(ListDataEvent arg0) {
	TableModelEvent tme = new TableModelEvent(this, arg0.getIndex0(), arg0.getIndex1(), TableModelEvent.ALL_COLUMNS,
		TableModelEvent.INSERT);

	for (TableModelListener tml : tableModelListeners) {
	    tml.tableChanged(tme);
	}

    }

    @Override
    public void intervalRemoved(ListDataEvent arg0) {
	TableModelEvent tme = new TableModelEvent(this, arg0.getIndex0(), arg0.getIndex1(), TableModelEvent.ALL_COLUMNS,
		TableModelEvent.DELETE);

	for (TableModelListener tml : tableModelListeners) {
	    tml.tableChanged(tme);
	}

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	return true;
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
	tableModelListeners.remove(l);

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	Person person = persons.get(rowIndex);

	switch (columnIndex) {
	case 0:
	    person.setLastName((String) aValue);
	    return;

	case 1:
	    person.setFirstName((String) aValue);
	    return;

	default:
	    throw new IndexOutOfBoundsException(Messages.getString("PersonTableModel.InvalidColumnIndexException")); //$NON-NLS-1$
	}
    }

}

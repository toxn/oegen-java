/**
 *
 */
package fr.cdbs.oegen.ui

import fr.cdbs.oegen.data.Person
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

/**
 * @author toxn
 */
class PersonTableModel(
    /**
     * The list containing the persons to handle
     */
    private val persons: PersonList
) : TableModel, ListDataListener {
    private val tableModelListeners: MutableSet<TableModelListener> = HashSet<TableModelListener>()

    /**
     *
     */
    init {
        persons.addListDataListener(this)
    }

    override fun addTableModelListener(l: TableModelListener?) {
        tableModelListeners.add(l!!)
    }

    override fun contentsChanged(arg0: ListDataEvent) {
        val tme = TableModelEvent(this, arg0.index0, arg0.index1, TableModelEvent.ALL_COLUMNS)

        for (tml in tableModelListeners) {
            tml.tableChanged(tme)
        }
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        when (columnIndex) {
            0 ->        // Lastname
                return String::class.java

            1 ->        // Firstname
                return String::class.java

            2 ->        // Id
                return String::class.java

            else -> throw RuntimeException(Messages.getString("PersonTableModel.InvalidColumnIndexException")) //$NON-NLS-1$

        }
    }

    /* !CodeTemplates.overridecomment.nonjd!
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    override fun getColumnCount(): Int {
        // Columns are Last Name, First name.
        return 3
    }

    override fun getColumnName(columnIndex: Int): String {
        return when (columnIndex) {
            0 ->        // Lastname
                Messages.getString("Person.lastName") //$NON-NLS-1$

            1 ->        // Firstname
                Messages.getString("Person.firstName") //$NON-NLS-1$

            2 ->        // Id
                Messages.getString("Oebject.Id") //$NON-NLS-1$

            else -> throw RuntimeException(Messages.getString("PersonTableModel.InvalidColumnIndexException")) //$NON-NLS-1$

        }
    }

    fun getPersonAt(rowIndex: Int): Person? {
        if (rowIndex < 0 || rowIndex >= persons.size()) throw RuntimeException(Messages.getString("PersonTableModel.InvalidRowIndexException")) //$NON-NLS-1$


        return persons.get(rowIndex)
    }

    @Throws(Exception::class)
    fun getRowOf(person: Person?): Int {
        var i = 0

        for (p in persons) {
            if (p == person) return i

            ++i
        }

        throw Exception()
    }

    /*
     * !CodeTemplates.overridecomment.nonjd!
     *
     * @see javax.swing.table.TableModel#getRowCount()
     */
    override fun getRowCount(): Int {
        return persons.size()
    }

    /*
     * !CodeTemplates.overridecomment.nonjd!
     *
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    override fun getValueAt(rowIndex: Int, colIndex: Int): Any? {
        if (rowIndex < 0 || rowIndex >= persons.size()) throw RuntimeException(Messages.getString("PersonTableModel.InvalidRowIndexException")) //$NON-NLS-1$


        val person = persons[rowIndex]

        when (colIndex) {
            0 -> return person!!.lastName

            1 -> return person!!.firstName

            2 -> return person!!.id
        }

        throw RuntimeException(Messages.getString("PersonTableModel.InvalidColumnIndexException")) //$NON-NLS-1$
    }

    override fun intervalAdded(arg0: ListDataEvent) {
        val tme = TableModelEvent(
            this, arg0.index0, arg0.index1, TableModelEvent.ALL_COLUMNS,
            TableModelEvent.INSERT
        )

        for (tml in tableModelListeners) {
            tml.tableChanged(tme)
        }
    }

    override fun intervalRemoved(arg0: ListDataEvent) {
        val tme = TableModelEvent(
            this, arg0.index0, arg0.index1, TableModelEvent.ALL_COLUMNS,
            TableModelEvent.DELETE
        )

        for (tml in tableModelListeners) {
            tml.tableChanged(tme)
        }
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    override fun removeTableModelListener(l: TableModelListener?) {
        tableModelListeners.remove(l)
    }

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        val person = persons[rowIndex]

        when (columnIndex) {
            0 -> {
                person!!.lastName = aValue as String?
                return
            }

            1 -> {
                person!!.firstName = aValue as String?
                return
            }

            else -> throw IndexOutOfBoundsException(Messages.getString("PersonTableModel.InvalidColumnIndexException")) //$NON-NLS-1$

        }
    }
}

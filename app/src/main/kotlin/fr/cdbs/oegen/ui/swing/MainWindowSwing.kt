package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.io.DatabaseFileFormat
import fr.cdbs.oegen.data.io.createExporter
import fr.cdbs.oegen.data.io.createImporter
import fr.cdbs.oegen.ui.MainWindow
import fr.cdbs.oegen.ui.Messages
import fr.cdbs.oegen.ui.PersonTableModel
import fr.cdbs.oegen.ui.swing.PersonSummary.PersonClickListener
import java.awt.CardLayout
import java.awt.Component
import java.awt.HeadlessException
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.print.PrinterException
import java.awt.print.PrinterJob
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.Box
import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTabbedPane
import javax.swing.JTable
import javax.swing.JToggleButton
import javax.swing.JOptionPane
import javax.swing.ListSelectionModel
import javax.swing.SwingConstants
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class MainWindowSwing : JFrame(), MainWindow, PersonClickListener {
    private val personTable: JTable = JTable()
    private lateinit var personRelationsPanel: JPanel
    private lateinit var personTree: PersonTree
    private lateinit var personViewPanel: JPanel

    private val personsTableModel = PersonTableModel(Person.persons)

    private val createPersonAction = object : CustomAction("Person.Create") {
        override fun actionPerformed(e: ActionEvent) {
            val newPerson = Person()
            try {
                personTable.requestFocusInWindow()
                personTable.editCellAt(
                    personTable.convertRowIndexToView(personsTableModel.getRowOf(newPerson)),
                    0,
                )
            } catch (ex: Exception) {
                Logger.getLogger(MainWindowSwing::class.java.name).log(Level.SEVERE, null, ex)
            }
        }
    }

    private val newAction = object : CustomAction("New") {
        override fun actionPerformed(e: ActionEvent) {
            if (hasRequest()) {
                Person.clearAll()
            }
        }
    }

    private val openAction = object : CustomAction("Open", FLAG_REQUEST) {
        override fun actionPerformed(e: ActionEvent) {
            open()
        }
    }

    private val personNextViewAction = object : CustomAction("Person.View.Next") {
        override fun actionPerformed(e: ActionEvent) {
            (personViewPanel.layout as CardLayout).next(personViewPanel)
        }
    }

    private val personPrevViewAction = object : CustomAction("Person.View.Prev") {
        override fun actionPerformed(e: ActionEvent) {
            (personViewPanel.layout as CardLayout).previous(personViewPanel)
        }
    }

    private val personRelationsPrintAction = object : CustomAction("Person.Relations.Print") {
        override fun actionPerformed(e: ActionEvent) {
            val pJob = PrinterJob.getPrinterJob()
            pJob.setPrintable(personTree)
            if (pJob.printDialog()) {
                try {
                    pJob.print()
                } catch (ex: PrinterException) {
                    System.err.println(ex)
                }
            }
        }
    }

    private val personRelationsRotateLeftAction = object : CustomAction("Person.Relations.Rotate.Left") {
        override fun actionPerformed(e: ActionEvent) {
            // Not implemented
        }
    }

    private val personRelationsRotateRightAction = object : CustomAction("Person.Relations.Rotate.Right") {
        override fun actionPerformed(e: ActionEvent) {
            // Not implemented
        }
    }

    private val personRelationsShowTextAction = object : CustomAction("Person.Relations.Show.Text") {
        override fun actionPerformed(e: ActionEvent) {
            (personRelationsPanel.layout as CardLayout).show(personRelationsPanel, getActionCommand())
        }
    }

    private val personRelationsShowTreeAction = object : CustomAction("Person.Relations.Show.Tree") {
        override fun actionPerformed(e: ActionEvent) {
            (personRelationsPanel.layout as CardLayout).show(personRelationsPanel, getActionCommand())
        }
    }

    private val personRelationsShowWheelAction = object : CustomAction("Person.Relations.Show.Wheel") {
        override fun actionPerformed(e: ActionEvent) {
            (personRelationsPanel.layout as CardLayout).show(personRelationsPanel, getActionCommand())
        }
    }

    private val personRelationsZoomInAction = object : CustomAction("Person.Relations.Zoom.In") {
        override fun actionPerformed(e: ActionEvent) {
            val newZoomLevel = personTree.generations - 1
            if (newZoomLevel == 1) {
                isEnabled = false
            }
            personTree.generations = newZoomLevel
        }
    }

    private val personRelationsZoomOutAction = object : CustomAction("Person.Relations.Zoom.Out") {
        override fun actionPerformed(e: ActionEvent) {
            personTree.generations = personTree.generations + 1
            personRelationsZoomInAction.isEnabled = true
        }
    }

    private val quitAction = object : CustomAction("Quit", FLAG_CONFIRM) {
        override fun actionPerformed(e: ActionEvent) {
            if (hasRequest() && !saveRequest()) return
            System.exit(0)
        }
    }

    private val removePersonsAction = object : CustomAction("Person.Remove", FLAG_CONFIRM) {
        override fun actionPerformed(e: ActionEvent) {
            val selectedRows = personTable.selectedRows
            val selectedPersons = selectedRows.map { row ->
                personsTableModel.getPersonAt(personTable.convertRowIndexToModel(row))
            }

            personTable.clearSelection()

            for (p in selectedPersons) {
                p?.remove()
            }
        }
    }

    private val saveAction = object : CustomAction("Save", FLAG_REQUEST) {
        override fun actionPerformed(e: ActionEvent) {
            save()
        }
    }

    private val saveAsAction = object : CustomAction("SaveAs", FLAG_REQUEST) {
        override fun actionPerformed(e: ActionEvent) {
            saveAs()
        }
    }

    private val searchPersonAction = object : CustomAction("Person.Search") {
        override fun actionPerformed(e: ActionEvent) {
            PersonSearch.showAndSelect(this@MainWindowSwing)?.let { jumpTo(it) }
        }
    }

    fun buildUI() {
        val menuBar = JMenuBar()
        jMenuBar = menuBar

        val fileMenu = JMenu(Messages.getString("MainWindow.FileMenu"))
        fileMenu.mnemonic = KeyEvent.VK_F
        menuBar.add(fileMenu)

        fileMenu.add(newAction)
        fileMenu.add(openAction)
        fileMenu.addSeparator()
        fileMenu.add(saveAction)
        fileMenu.add(saveAsAction)
        fileMenu.addSeparator()
        fileMenu.add(quitAction)

        val personListCard = Box.createVerticalBox()
        val personListButtons = Box.createHorizontalBox()

        personListButtons.add(JButton(createPersonAction))

        val remPersonBtn = JButton(removePersonsAction)
        removePersonsAction.isEnabled = false
        personListButtons.add(remPersonBtn)

        personListButtons.add(JButton(searchPersonAction))
        personListCard.add(personListButtons)

        personTable.model = personsTableModel
        personTable.setAutoCreateRowSorter(true)
        personListCard.add(JScrollPane(personTable))

        val personRelationsCard = Box.createVerticalBox()
        val personRelationsButtons = Box.createHorizontalBox()
        personRelationsCard.add(personRelationsButtons)

        val personRelationsType = ButtonGroup()

        val personRelationsTextButton = JToggleButton(personRelationsShowTextAction)
        personRelationsButtons.add(personRelationsTextButton, Component.LEFT_ALIGNMENT)
        personRelationsType.add(personRelationsTextButton)
        personRelationsTextButton.isSelected = true

        val personRelationsTreeButton = JToggleButton(personRelationsShowTreeAction)
        personRelationsButtons.add(personRelationsTreeButton, Component.LEFT_ALIGNMENT)
        personRelationsType.add(personRelationsTreeButton)

        val personRelationsWheelButton = JToggleButton(personRelationsShowWheelAction)
        personRelationsButtons.add(personRelationsWheelButton, Component.LEFT_ALIGNMENT)
        personRelationsType.add(personRelationsWheelButton)

        personRelationsButtons.add(Box.createHorizontalGlue())

        val personRelationsZoomInButton = JButton(personRelationsZoomInAction)
        personRelationsZoomInAction.isEnabled = true
        personRelationsButtons.add(personRelationsZoomInButton, Component.RIGHT_ALIGNMENT)

        val personRelationsZoomOutButton = JButton(personRelationsZoomOutAction)
        personRelationsZoomOutAction.isEnabled = true
        personRelationsButtons.add(personRelationsZoomOutButton, Component.RIGHT_ALIGNMENT)

        personRelationsButtons.add(JButton(personRelationsRotateLeftAction), Component.RIGHT_ALIGNMENT)
        personRelationsButtons.add(JButton(personRelationsRotateRightAction), Component.RIGHT_ALIGNMENT)
        personRelationsButtons.add(JButton(personRelationsPrintAction))

        val personSummary = PersonSummary()
        personSummary.addPersonClickListener(this)

        personTree = PersonTree()

        val personWheel = JPanel()
        personRelationsPanel = JPanel(CardLayout())
        personRelationsPanel.add(JScrollPane(personSummary), personRelationsShowTextAction.getActionCommand())
        personRelationsPanel.add(JScrollPane(personTree), personRelationsShowTreeAction.getActionCommand())
        personRelationsPanel.add(JScrollPane(personWheel), personRelationsShowWheelAction.getActionCommand())

        personRelationsShowTextAction.isEnabled = true
        personRelationsCard.add(personRelationsPanel)

        val personEditorCard = PersonEditor()
        val personEditorScrollPane = JScrollPane(personEditorCard)

        val personViewPane = Box.createHorizontalBox()
        val prevPersonViewBtn = JButton(personPrevViewAction)
        personPrevViewAction.isEnabled = false
        personViewPane.add(prevPersonViewBtn)

        personViewPanel = JPanel(CardLayout())
        val personCardCommandList = "LIST"
        val personCardCommandGraph = "GRAPH"
        val personCardCommandDetails = "DETAILS"

        personViewPanel.add(personListCard, personCardCommandList)
        personViewPanel.add(personRelationsCard, personCardCommandGraph)
        personViewPanel.add(personEditorScrollPane, personCardCommandDetails)
        personViewPane.add(personViewPanel)

        val nextPersonViewBtn = JButton(personNextViewAction)
        personNextViewAction.isEnabled = false
        personViewPane.add(nextPersonViewBtn)

        val objectPane = JTabbedPane(SwingConstants.LEFT)
        objectPane.addTab(Messages.getString("Person.Person"), personViewPane)

        contentPane.add(objectPane)
        defaultCloseOperation = DISPOSE_ON_CLOSE

        personTable.selectionModel.addListSelectionListener { e ->
            val lsm = personTable.selectionModel
            val bSelectionExists = !lsm.isSelectionEmpty
            prevPersonViewBtn.isEnabled = bSelectionExists
            nextPersonViewBtn.isEnabled = bSelectionExists
            remPersonBtn.isEnabled = bSelectionExists

            if (bSelectionExists) {
                val currentPerson = Person.persons.elementAt(lsm.leadSelectionIndex)
                personSummary.center = currentPerson
                personTree.center = currentPerson
                personEditorCard.person = currentPerson
            }
        }

        Person.persons.addListDataListener(object : ListDataListener {
            override fun contentsChanged(e: ListDataEvent) = dataChanged()
            override fun intervalAdded(e: ListDataEvent) = dataChanged()
            override fun intervalRemoved(e: ListDataEvent) = dataChanged()
        })
    }

    fun jumpTo(person: Person?) {
        person ?: return
        for (i in 0 until personTable.rowCount) {
            if (personsTableModel.getPersonAt(personTable.convertRowIndexToModel(i)) === person) {
                personTable.changeSelection(i, 0, false, false)
                break
            }
        }
    }

    fun open() {
        if (openAction.hasConfirm() && !saveRequest()) return

        when (fileChooser.showOpenDialog(this@MainWindowSwing)) {
            JFileChooser.APPROVE_OPTION -> {
                Person.clearAll()
                try {
                    FileInputStream(fileChooser.selectedFile).use { fis ->
                        createImporter(fis, DatabaseFileFormat.fromFile(fileChooser.selectedFile)).doImport()
                        saveFile = fileChooser.selectedFile
                    }
                } catch (e1: FileNotFoundException) {
                    JOptionPane.showMessageDialog(
                        this@MainWindowSwing,
                        e1,
                        Messages.getString("MainWindowSwing.DialogTitle.Error"),
                        JOptionPane.ERROR_MESSAGE,
                    )
                } catch (e2: IOException) {
                    JOptionPane.showMessageDialog(
                        this@MainWindowSwing,
                        e2,
                        Messages.getString("MainWindowSwing.DialogTitle.Error"),
                        JOptionPane.ERROR_MESSAGE,
                    )
                }
            }
        }
    }

    override fun personClick(pce: PersonSummary.PersonClickEvent) {
        jumpTo(pce.person)
    }

    fun save() {
        if (saveFile == null) {
            saveAs()
            return
        }

        try {
            FileOutputStream(saveFile).use { fos ->
                createExporter(fos, DatabaseFileFormat.fromFile(saveFile!!)).doExport()
            }
            isSavedToDisk = true
        } catch (e1: FileNotFoundException) {
            JOptionPane.showMessageDialog(
                this@MainWindowSwing,
                e1,
                Messages.getString("MainWindowSwing.DialogTitle.Error"),
                JOptionPane.ERROR_MESSAGE,
            )
        } catch (e2: IOException) {
            JOptionPane.showMessageDialog(
                this@MainWindowSwing,
                e2,
                Messages.getString("MainWindowSwing.DialogTitle.Error"),
                JOptionPane.ERROR_MESSAGE,
            )
        }
    }

    fun saveAs() {
        when (fileChooser.showSaveDialog(this@MainWindowSwing)) {
            JFileChooser.APPROVE_OPTION -> {
                saveFile = fileChooser.selectedFile
                save()
            }
        }
    }

    fun dataChanged() {
        isSavedToDisk = false
        quitAction.setRequest(true)
        saveAction.setRequest(true)

        if (Person.persons.isEmpty()) {
            newAction.isEnabled = false
            newAction.setRequest(false)
            openAction.setRequest(false)
        } else {
            newAction.setRequest(true)
            openAction.setRequest(true)
        }
    }

    fun saveRequest(): Boolean {
        val options = arrayOf(
            Messages.getString("MainWindowSwing.DialogOption.Continue"),
            Messages.getString("MainWindowSwing.DialogOption.SaveFirst"),
            Messages.getString("MainWindowSwing.DialogOption.Cancel"),
        )
        val res = JOptionPane.showOptionDialog(
            this@MainWindowSwing,
            Messages.getString("MainWindowSwing.DataLostWarning.Message"),
            Messages.getString("MainWindowSwing.DataLostWarning.Title"),
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[2],
        )

        return when (res) {
            JOptionPane.CLOSED_OPTION, 2 -> false
            0 -> true
            1 -> {
                save()
                true
            }
            else -> throw RuntimeException("Unexpected dialog result: $res")
        }
    }

    companion object {
        val fileChooser: JFileChooser = JFileChooser().apply {
            fileFilter = object : FileFilter() {
                override fun accept(file: File): Boolean {
                    if (file.isDirectory) return true
                    return DatabaseFileFormat.fromFileName(file.name) != null
                }

                override fun getDescription(): String =
                    Messages.getString("DatabaseFileFilter.description")
            }
        }
        var isSavedToDisk: Boolean = true
        var saveFile: File? = null
    }
}

/**
 *
 */
package com.cdbs.oegen.ui.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;

import com.cdbs.oegen.data.Person;
import com.cdbs.oegen.data.io.xml.Exporter;
import com.cdbs.oegen.data.io.xml.Importer;
import com.cdbs.oegen.ui.Messages;
import com.cdbs.oegen.ui.PersonTableModel;
import com.cdbs.oegen.ui.swing.PersonSummary.PersonClickListener;

/**
 * @author toxn
 *
 */
public final class MainWindowSwing extends com.cdbs.oegen.ui.MainWindow implements ActionListener, PersonClickListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * True if data is in the same state as in the save file.
     */
    static boolean isSavedToDisk = true;
    static JFileChooser fileChooser = new JFileChooser();

    private static final String CMD_ADD_PERSON = "ADD PERSON";
    private static final String CMD_REMOVE_PERSON = "REMOVE PERSON";

    /**
     * The file where the data originate. receives normal save.
     */
    static File saveFile = null;
    private static final String CMD_PERSON_RELATIONS_TEXT = "TEXT";
    private static final String CMD_PERSON_RELATIONS_TREE = "TREE";

    private static final String CMD_PERSON_RELATIONS_WHEEL = "WHEEL";
    private static final String CMD_PERSON_RELATIONS_ZOOM_IN = "ZOOM IN";

    private final JTable personTable;

    private final CustomAction newAction = new CustomAction("New") { //$NON-NLS-1$
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (hasRequest()) {
		Person.persons.clear();
	    }
	}

    };

    private final CustomAction openAction = new CustomAction("Open", //$NON-NLS-1$
	    CustomAction.FLAG_REQUEST) {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    open();
	}
    };

    private final CustomAction saveAction = new CustomAction("Save", CustomAction.FLAG_REQUEST) { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    save();
	}

    };
    private final CustomAction saveAsAction = new CustomAction("SaveAs", CustomAction.FLAG_REQUEST) { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    saveAs();
	}

    };
    private final CustomAction quitAction = new CustomAction("Quit", CustomAction.FLAG_CONFIRM) { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (hasRequest())
		if (!saveRequest())
		    return;

	    System.exit(0);
	}
    };
    private final PersonTableModel personsTableModel = new PersonTableModel(Person.persons);
    private JPanel personRelationsPanel;
    private JButton personRelationsTextButton;
    private JButton personRelationsTreeButton;
    private JButton personRelationsWheelButton;
    private JButton personRelationsZoomInButton;
    private JButton personRelationsZoomOutButton;
    private JButton personRelationsRotateLeftButton;
    private JButton personRelationsRotateRightButton;
    private JButton personRelationsPrintButton;
    private PersonTree personTree;

    /**
     * @throws HeadlessException
     */
    public MainWindowSwing() throws HeadlessException {
	super();

	personTable = new JTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	switch (command) {
	case CMD_ADD_PERSON:
	    Person newPerson = new Person();
	    {
		try {
		    personTable.requestFocusInWindow();
		    personTable.editCellAt(personTable.convertRowIndexToView(personsTableModel.getRowOf(newPerson)), 0);
		} catch (Exception ex) {
		    Logger.getLogger(MainWindowSwing.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	    break;

	case CMD_REMOVE_PERSON:
	    Person currentPerson = Person.persons.elementAt(personTable.getSelectedRow());
	    Person.persons.removeElement(currentPerson);
	    break;

	case CMD_PERSON_RELATIONS_TEXT:
	case CMD_PERSON_RELATIONS_TREE:
	case CMD_PERSON_RELATIONS_WHEEL:
	    personRelationsTextButton.setEnabled(command != CMD_PERSON_RELATIONS_TEXT);
	    personRelationsTreeButton.setEnabled(command != CMD_PERSON_RELATIONS_TREE);
	    personRelationsWheelButton.setEnabled(command != CMD_PERSON_RELATIONS_WHEEL);

	    ((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, command);
	    break;

	case CMD_PERSON_RELATIONS_ZOOM_IN:
	    try {
		personTree.setGenerations(personTree.getGenerations() + 1);
		// personWheel.setGenerations(personWheel.getGenerations() +
		// 1);
		personRelationsZoomOutButton.setEnabled(true);
	    } catch (Exception ex) {
		throw new RuntimeException(ex);
	    }
	    break;
	}
    }

    public void buildUI() {
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);

	JMenu fileMenu = new JMenu(Messages.getString("MainWindowSwing.FileMenu")); //$NON-NLS-1$
	fileMenu.setMnemonic(KeyEvent.VK_F);
	menuBar.add(fileMenu);

	fileMenu.add(newAction);
	fileMenu.add(openAction);
	fileMenu.addSeparator();
	fileMenu.add(saveAction);
	fileMenu.add(saveAsAction);
	fileMenu.addSeparator();
	fileMenu.add(quitAction);

	/**
	 * Contains a list of persons along with a toolbar to create, delete or
	 * filter
	 */
	Box personListCard = Box.createVerticalBox();
	Box personListButtons = Box.createHorizontalBox();

	JButton addPersonBtn = new JButton("+"); //$NON-NLS-1$
	addPersonBtn.setActionCommand(CMD_ADD_PERSON);
	personListButtons.add(addPersonBtn);
	addPersonBtn.addActionListener(this);

	final JButton remPersonBtn = new JButton("\u2715"); //$NON-NLS-1$
	remPersonBtn.setEnabled(false);
	remPersonBtn.setActionCommand(CMD_REMOVE_PERSON);
	remPersonBtn.addActionListener(this);
	personListButtons.add(remPersonBtn);

	JButton searchPersonBtn = new JButton(Messages.getString("MainWindow.Search")); //$NON-NLS-1$
	personListButtons.add(searchPersonBtn);

	personListCard.add(personListButtons);

	personTable.setModel(personsTableModel);
	personTable.setAutoCreateRowSorter(true);
	// personTable.setTableHeader(null);

	personListCard.add(new JScrollPane(personTable));

	/**
	 * Display the relations of the person with others in the database.
	 *
	 * There is 3 modes of display : - a textual display summarying parents,
	 * siblings and children along with spouses, - a tree centering on the
	 * current person and displaying ascendant and/or descendant. Buttons
	 * control zoom level (eg number of ascending and descending
	 * generations) - a wheel centering on the current person and displaying
	 * ascendant and/or descendant. Buttons control zoom level (eg number of
	 * ascending and descending generations)
	 */
	Box personRelationsCard = Box.createVerticalBox();

	Box personRelationsButtons = Box.createHorizontalBox();
	personRelationsCard.add(personRelationsButtons);

	personRelationsTextButton = new JButton(Messages.getString("MainWindow.Text"));
	personRelationsTextButton.setEnabled(false);
	personRelationsButtons.add(personRelationsTextButton, Component.LEFT_ALIGNMENT);

	personRelationsTreeButton = new JButton(Messages.getString("MainWindow.Tree"));
	personRelationsTreeButton.setEnabled(true);
	personRelationsButtons.add(personRelationsTreeButton, Component.LEFT_ALIGNMENT);

	personRelationsWheelButton = new JButton(Messages.getString("MainWindow.Wheel"));
	personRelationsWheelButton.setEnabled(true);
	personRelationsButtons.add(personRelationsWheelButton, Component.LEFT_ALIGNMENT);

	personRelationsButtons.add(Box.createHorizontalGlue());

	personRelationsZoomInButton = new JButton("+");
	personRelationsZoomInButton.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomInButton, Component.RIGHT_ALIGNMENT);

	personRelationsZoomOutButton = new JButton("-");
	personRelationsZoomOutButton.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomOutButton, Component.RIGHT_ALIGNMENT);

	personRelationsRotateLeftButton = new JButton("<-\\");
	personRelationsRotateLeftButton.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateLeftButton, Component.RIGHT_ALIGNMENT);

	personRelationsRotateRightButton = new JButton("/->");
	personRelationsRotateRightButton.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateRightButton, Component.RIGHT_ALIGNMENT);

	personRelationsPrintButton = new JButton(Messages.getString("MainWindow.Print"));
	personRelationsPrintButton.setEnabled(true);
	personRelationsButtons.add(personRelationsPrintButton);

	final PersonSummary personSummary = new PersonSummary();
	JScrollPane personSummaryScrollPane = new JScrollPane(personSummary);

	personSummary.addPersonClickListener(this);

	personTree = new PersonTree();
	JScrollPane personTreeScrollPane = new JScrollPane(personTree);

	final JComponent personWheel = new JPanel(); // TODO implement a custom
	// component
	JScrollPane personWheelScrollPane = new JScrollPane(personWheel);

	personRelationsPanel = new JPanel(new CardLayout());

	personRelationsPanel.add(personSummaryScrollPane, CMD_PERSON_RELATIONS_TEXT);
	personRelationsPanel.add(personTreeScrollPane, CMD_PERSON_RELATIONS_TREE);
	personRelationsPanel.add(personWheelScrollPane, CMD_PERSON_RELATIONS_WHEEL);

	((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, CMD_PERSON_RELATIONS_TEXT);

	personRelationsTextButton.setActionCommand(CMD_PERSON_RELATIONS_TEXT);
	personRelationsTextButton.addActionListener(this);

	personRelationsTreeButton.setActionCommand(CMD_PERSON_RELATIONS_TREE);
	personRelationsTreeButton.addActionListener(this);

	personRelationsWheelButton.setActionCommand(CMD_PERSON_RELATIONS_WHEEL);
	personRelationsWheelButton.addActionListener(this);

	personRelationsZoomInButton.setActionCommand(CMD_PERSON_RELATIONS_ZOOM_IN);
	personRelationsZoomInButton.addActionListener(this);

	personRelationsZoomOutButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    int newZoomLevel = personTree.getGenerations() - 1;

		    if (newZoomLevel == 1) {
			personRelationsZoomOutButton.setEnabled(false);
		    }

		    personTree.setGenerations(newZoomLevel);
		    // personWheel.setGenerations(newZoomLevel);
		} catch (Exception ex) {
		    throw new RuntimeException(ex);
		}

	    }
	});

	personRelationsCard.add(personRelationsPanel);
	/**
	 * Enable fine editing of person's details such as name, events and
	 * relations
	 */
	final PersonEditor personEditorCard = new PersonEditor();

	JScrollPane personEditorScrollPane = new JScrollPane(personEditorCard);

	/**
	 * The type of person view (3 types : list, graphical, details)
	 */
	Box personViewPane = Box.createHorizontalBox();

	/**
	 * A button that switchs from person Details to person graph, or from
	 * person graph to person list
	 */
	final JButton prevPersonViewBtn = new JButton("<"); //$NON-NLS-1$
	prevPersonViewBtn.setEnabled(false);
	personViewPane.add(prevPersonViewBtn);

	/**
	 * The container that displays list, graph or detail view
	 */
	final JPanel personViewPanel = new JPanel(new CardLayout());

	final String personCardCommandList = "LIST"; //$NON-NLS-1$
	final String personCardCommandGraph = "GRAPH"; //$NON-NLS-1$
	final String personCardCommandDetails = "DETAILS"; //$NON-NLS-1$

	personViewPanel.add(personListCard, personCardCommandList);
	personViewPanel.add(personRelationsCard, personCardCommandGraph);
	personViewPanel.add(personEditorScrollPane, personCardCommandDetails);
	personViewPane.add(personViewPanel);

	/**
	 * A button than switches from person list to person graph or from
	 * person graph to person details
	 */
	final JButton nextPersonViewBtn = new JButton(">"); //$NON-NLS-1$
	nextPersonViewBtn.setEnabled(false);
	personViewPane.add(nextPersonViewBtn);

	/**
	 * This permits to select type of object the user wants to work with
	 * (Person, Place, Source, etc)
	 *
	 */
	JTabbedPane objectPane = new JTabbedPane(SwingConstants.LEFT);
	objectPane.addTab(Messages.getString("Person.Person"), personViewPane); //$NON-NLS-1$

	// getContentPane().add(new JScrollPane(objectPane));
	getContentPane().add(objectPane);

	// Various notifications and triggers
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	// Connect the button to the Cards
	prevPersonViewBtn.addActionListener((ActionEvent e) -> {
	    ((CardLayout) personViewPanel.getLayout()).previous(personViewPanel);
	});

	nextPersonViewBtn.addActionListener((ActionEvent e) -> {
	    ((CardLayout) personViewPanel.getLayout()).next(personViewPanel);
	});

	// Disable Remove and prev/next card when nothing is selected in list
	personTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
	    ListSelectionModel lsm = personTable.getSelectionModel();
	    boolean bSelectionExists = !lsm.isSelectionEmpty();
	    prevPersonViewBtn.setEnabled(bSelectionExists);
	    nextPersonViewBtn.setEnabled(bSelectionExists);
	    remPersonBtn.setEnabled(bSelectionExists);

	    Person currentPerson = Person.persons.elementAt(lsm.getLeadSelectionIndex());
	    personSummary.setCenter(currentPerson);
	    personTree.setCenter(currentPerson);
	    personEditorCard.setPerson(currentPerson);
	});

	personRelationsPrintButton.addActionListener((ActionEvent e) -> {
	    PrinterJob pJob = PrinterJob.getPrinterJob();
	    pJob.setPrintable(personTree);
	    boolean doPrint = pJob.printDialog();

	    if (doPrint) {
		try {
		    pJob.print();
		} catch (PrinterException ex) {
		    System.err.println(ex);
		}
	    }
	});

	Person.persons.addListDataListener(new ListDataListener() {

	    @Override
	    public void contentsChanged(ListDataEvent e) {
		// Database is not in synch with last save
		dataChanged();
	    }

	    @Override
	    public void intervalAdded(ListDataEvent e) {
		// Database is not in synch with last save
		dataChanged();
	    }

	    @Override
	    public void intervalRemoved(ListDataEvent e) {
		// Database is not in synch with last save
		dataChanged();
	    }
	});

    }

    void dataChanged() {
	isSavedToDisk = false;

	quitAction.setRequest(true);
	saveAction.setRequest(true);

	if (Person.persons.isEmpty()) {
	    newAction.setEnabled(false);
	    newAction.setRequest(false);
	    openAction.setRequest(false);
	} else {
	    newAction.setRequest(true);
	    openAction.setRequest(true);
	}
    }

    public void jumpTo(Person person) {
	for(int i = 0; i < personTable.getRowCount(); i++) {
	    if(personsTableModel.getPersonAt(personTable.convertRowIndexToModel(i)) == person) {
		personTable.changeSelection(i, 0, false, false);
		break; // stop the loop
	    }

	}
    }

    public void open() {
	if (openAction.hasConfirm())
	    if (!saveRequest())
		return;

	int result = fileChooser.showOpenDialog(MainWindowSwing.this);

	switch (result) {
	case JFileChooser.APPROVE_OPTION:
	    Person.persons.clear();
	    try (FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile())) {
		new Importer(fis).doImport();
		saveFile = fileChooser.getSelectedFile();
	    } catch (FileNotFoundException e1) {
		JOptionPane.showMessageDialog(MainWindowSwing.this, e1, Messages.getString("MainWindowSwing.DialogTitle.Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	    } catch (IOException e2) {
		JOptionPane.showMessageDialog(MainWindowSwing.this, e2,
			Messages.getString("MainWindowSwing.DialogTitle.Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	    }

	    break;

	case JFileChooser.CANCEL_OPTION:
	case JFileChooser.ERROR_OPTION:
	    return;
	}
    }

    @Override
    public void personClick(PersonSummary.PersonClickEvent pce) {
	jumpTo(pce.getPerson());
    }

    public void save() {
	if (saveFile == null) {
	    saveAs();
	}

	try (FileOutputStream fos = new FileOutputStream(saveFile)) {
	    new Exporter(fos).doExport();
	} catch (FileNotFoundException e1) {
	    JOptionPane.showMessageDialog(MainWindowSwing.this, e1,
		    Messages.getString("MainWindowSwing.DialogTitle.Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	} catch (IOException e2) {
	    JOptionPane.showMessageDialog(MainWindowSwing.this, e2,
		    Messages.getString("MainWindowSwing.DialogTitle.Error"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}
    }

    public void saveAs() {
	int result = fileChooser.showSaveDialog(MainWindowSwing.this);

	switch (result) {
	case JFileChooser.APPROVE_OPTION:
	    saveFile = fileChooser.getSelectedFile();
	    save();
	    break;

	case JFileChooser.CANCEL_OPTION:
	case JFileChooser.ERROR_OPTION:
	    return;
	}
    }

    boolean saveRequest() {
	String[] options = { Messages.getString("MainWindowSwing.DialogOption.Continue"), Messages.getString("MainWindowSwing.DialogOption.SaveFirst"), Messages.getString("MainWindowSwing.DialogOption.Cancel") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	int res = JOptionPane.showOptionDialog(MainWindowSwing.this,
		Messages.getString("MainWindowSwing.DataLostWarning.Message"), //$NON-NLS-1$
		Messages.getString("MainWindowSwing.DataLostWarning.Title"), JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, //$NON-NLS-1$
		options[2]);

	switch (res) {
	case JOptionPane.CLOSED_OPTION:
	    // Fallthrough
	case 2:
	    return false;

	case 0: // Continue
	    return true;

	case 1: // Save first
	    // Let's save
	    save();
	    // and proceed further
	    return true;

	default:
	    // Shouldn't occur
	    throw new RuntimeException();
	}
    }
}

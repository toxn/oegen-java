/**
 *
 */
package com.cdbs.oegen.ui.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
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
import javax.swing.ButtonGroup;
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
import javax.swing.JToggleButton;
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
public final class MainWindowSwing extends com.cdbs.oegen.ui.MainWindow implements PersonClickListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static JFileChooser fileChooser = new JFileChooser();
    /**
     * True if data is in the same state as in the save file.
     */
    static boolean isSavedToDisk = true;

    private static final String CMD_ADD_PERSON = "ADD PERSON"; //$NON-NLS-1$
    private static final String CMD_REMOVE_PERSON = "REMOVE PERSON"; //$NON-NLS-1$

    /**
     * The file where the data originate. receives normal save.
     */
    static File saveFile = null;

    private final JTable personTable;

    private final CustomAction createPersonAction = new CustomAction("Person.Create") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    Person newPerson=new Person();{try{personTable.requestFocusInWindow();personTable.editCellAt(personTable.convertRowIndexToView(personsTableModel.getRowOf(newPerson)),0);}catch(Exception ex){Logger.getLogger(MainWindowSwing.class.getName()).log(Level.SEVERE,null,ex);}}

	}
    };

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

    private final CustomAction personNextViewAction = new CustomAction("Person.View.Next") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    ((CardLayout) personViewPanel.getLayout()).next(personViewPanel);
	}

    };

    private final CustomAction personPrevViewAction = new CustomAction("Person.View.Prev") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    ((CardLayout) personViewPanel.getLayout()).previous(personViewPanel);
	}

    };

    private JPanel personRelationsPanel;

    private final CustomAction personRelationsPrintAction = new CustomAction("Person.Relations.Print") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
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
	}

    };

    private final CustomAction personRelationsRotateLeftAction = new CustomAction("Person.Relations.Rotate.Left") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    // TODO To be implemented
	}

    };

    private final CustomAction personRelationsRotateRightAction = new CustomAction("Person.Relations.Rotate.Right") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    // TODO To be implemented
	}

    };

    private final CustomAction personRelationsShowTextAction = new CustomAction("Person.Relations.Show.Text") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    ((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, getActionCommand());

	}

    };

    private final CustomAction personRelationsShowTreeAction = new CustomAction("Person.Relations.Show.Tree") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    ((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, getActionCommand());

	}

    };

    private final CustomAction personRelationsShowWheelAction = new CustomAction("Person.Relations.Show.Wheel") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    ((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, getActionCommand());

	}

    };

    private final CustomAction personRelationsZoomInAction = new CustomAction("Person.Relations.Zoom.In") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    try {
		int newZoomLevel = personTree.getGenerations() - 1;

		if (newZoomLevel == 1) {
		    personRelationsZoomInAction.setEnabled(false);
		}

		personTree.setGenerations(newZoomLevel);
		// personWheel.setGenerations(newZoomLevel);
	    } catch (Exception ex) {
		throw new RuntimeException(ex);
	    }
	}

    };

    private final CustomAction personRelationsZoomOutAction = new CustomAction("Person.Relations.Zoom.Out") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {

	    try {
		personTree.setGenerations(personTree.getGenerations() + 1);
		// personWheel.setGenerations(personWheel.getGenerations() +
		// 1);
		personRelationsZoomInAction.setEnabled(true);
	    } catch (Exception ex) {
		throw new RuntimeException(ex);
	    }
	}

    };

    private PersonTree personTree;
    private JPanel personViewPanel;

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

    private final CustomAction removePersonsAction = new CustomAction("Person.Remove", CustomAction.FLAG_CONFIRM) { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    int[]selectedRows=personTable.getSelectedRows();Person[]selectedPersons=new Person[selectedRows.length];

	    int i=0;for(int j:selectedRows){selectedPersons[i++]=personsTableModel.getPersonAt(personTable.convertRowIndexToModel(j));}

	    personTable.clearSelection();

	    for(Person p:selectedPersons){p.remove();}
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

    private final CustomAction searchPersonAction = new CustomAction("Person.Search") { //$NON-NLS-1$

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void actionPerformed(ActionEvent e) {
	    // TODO Auto-generated method stub

	}

    };

    /**
     * @throws HeadlessException
     */
    public MainWindowSwing() throws HeadlessException {
	super();

	personTable = new JTable();
    }


    public void buildUI() {
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);

	JMenu fileMenu = new JMenu(Messages.getString("MainWindow.FileMenu")); //$NON-NLS-1$
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


	JButton addPersonBtn = new JButton(createPersonAction);
	personListButtons.add(addPersonBtn);

	final JButton remPersonBtn = new JButton(removePersonsAction);
	removePersonsAction.setEnabled(false);
	personListButtons.add(remPersonBtn);

	JButton searchPersonBtn = new JButton(searchPersonAction);
	personListButtons.add(searchPersonBtn);

	personListCard.add(personListButtons);

	personTable.setModel(personsTableModel);
	personTable.setAutoCreateRowSorter(true);

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

	ButtonGroup personRelationsType = new ButtonGroup();

	final JToggleButton personRelationsTextButton = new JToggleButton(personRelationsShowTextAction);
	personRelationsButtons.add(personRelationsTextButton, Component.LEFT_ALIGNMENT);
	personRelationsType.add(personRelationsTextButton);
	personRelationsTextButton.setSelected(true);

	final JToggleButton personRelationsTreeButton = new JToggleButton(personRelationsShowTreeAction);
	personRelationsButtons.add(personRelationsTreeButton, Component.LEFT_ALIGNMENT);
	personRelationsType.add(personRelationsTreeButton);

	final JToggleButton personRelationsWheelButton = new JToggleButton(personRelationsShowWheelAction);
	personRelationsButtons.add(personRelationsWheelButton, Component.LEFT_ALIGNMENT);
	personRelationsType.add(personRelationsWheelButton);

	personRelationsButtons.add(Box.createHorizontalGlue());

	final JButton personRelationsZoomInButton = new JButton(personRelationsZoomInAction);
	personRelationsZoomInAction.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomInButton, Component.RIGHT_ALIGNMENT);

	final JButton personRelationsZoomOutButton = new JButton(personRelationsZoomOutAction);
	personRelationsZoomOutAction.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomOutButton, Component.RIGHT_ALIGNMENT);

	JButton personRelationsRotateLeftButton = new JButton(personRelationsRotateLeftAction);
	personRelationsRotateLeftAction.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateLeftButton, Component.RIGHT_ALIGNMENT);

	JButton personRelationsRotateRightButton = new JButton(personRelationsRotateRightAction);
	personRelationsRotateRightAction.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateRightButton, Component.RIGHT_ALIGNMENT);

	JButton personRelationsPrintButton = new JButton(personRelationsPrintAction);
	personRelationsPrintAction.setEnabled(true);
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

	personRelationsPanel.add(personSummaryScrollPane, personRelationsShowTextAction.getActionCommand());
	personRelationsPanel.add(personTreeScrollPane, personRelationsShowTreeAction.getActionCommand());
	personRelationsPanel.add(personWheelScrollPane, personRelationsShowWheelAction.getActionCommand());

	personRelationsShowTextAction.setEnabled(true);

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
	final JButton prevPersonViewBtn = new JButton(personPrevViewAction);
	personPrevViewAction.setEnabled(false);
	personViewPane.add(prevPersonViewBtn);

	personViewPanel = new JPanel(new CardLayout());

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
	final JButton nextPersonViewBtn = new JButton(personNextViewAction);
	personNextViewAction.setEnabled(false);
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

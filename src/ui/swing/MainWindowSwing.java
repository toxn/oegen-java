/**
 *
 */
package ui.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Person;
import ui.Messages;

/**
 * @author toxn
 *
 */
public final class MainWindowSwing extends ui.MainWindow {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @throws HeadlessException
     */
    public MainWindowSwing() throws HeadlessException {
	super();

	/**
	 * Contains a list of persons along with a toolbar to create, delete or
	 * filter
	 */
	Box personListCard = Box.createVerticalBox();
	Box personListButtons = Box.createHorizontalBox();

	JButton addPersonBtn = new JButton("+"); //$NON-NLS-1$
	personListButtons.add(addPersonBtn);

	final JButton remPersonBtn = new JButton("×"); //$NON-NLS-1$
	remPersonBtn.setEnabled(false);
	personListButtons.add(remPersonBtn);

	JButton searchPersonBtn = new JButton(Messages.getString("MainWindow.Search")); //$NON-NLS-1$
	personListButtons.add(searchPersonBtn);

	personListCard.add(personListButtons);

	final JList<Person> personList = new JList<Person>(Person.persons);
	personList.setCellRenderer(new ListCellRenderer<Person>() {

	    @Override
	    public Component getListCellRendererComponent(JList<? extends Person> list, Person value, int index,
		    boolean isSelected, boolean cellHasFocus) {
		return new JLabel(value.toString());
	    }
	});

	personListCard.add(personList);

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

	final JButton personRelationsTextButton = new JButton(Messages.getString("MainWindow.Text")); //$NON-NLS-1$
	personRelationsTextButton.setEnabled(false);
	personRelationsButtons.add(personRelationsTextButton, Component.LEFT_ALIGNMENT);

	final JButton personRelationsTreeButton = new JButton(Messages.getString("MainWindow.Tree")); //$NON-NLS-1$
	personRelationsTreeButton.setEnabled(true);
	personRelationsButtons.add(personRelationsTreeButton, Component.LEFT_ALIGNMENT);

	final JButton personRelationsWheelButton = new JButton(Messages.getString("MainWindow.Wheel")); //$NON-NLS-1$
	personRelationsWheelButton.setEnabled(true);
	personRelationsButtons.add(personRelationsWheelButton, Component.LEFT_ALIGNMENT);

	personRelationsButtons.add(Box.createHorizontalGlue());

	final JButton personRelationsZoomInButton = new JButton("+"); //$NON-NLS-1$
	personRelationsZoomInButton.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomInButton, Component.RIGHT_ALIGNMENT);

	final JButton personRelationsZoomOutButton = new JButton("-"); //$NON-NLS-1$
	personRelationsZoomOutButton.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomOutButton, Component.RIGHT_ALIGNMENT);

	JButton personRelationsRotateLeftButton = new JButton("<-\\"); //$NON-NLS-1$
	personRelationsRotateLeftButton.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateLeftButton, Component.RIGHT_ALIGNMENT);

	JButton personRelationsRotateRightButton = new JButton("/->"); //$NON-NLS-1$
	personRelationsRotateRightButton.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateRightButton, Component.RIGHT_ALIGNMENT);

	JButton personRelationsPrintButton = new JButton(Messages.getString("MainWindow.Print")); //$NON-NLS-1$
	personRelationsPrintButton.setEnabled(true);
	personRelationsButtons.add(personRelationsPrintButton);

	JComponent personSummary = new JPanel(); // TODO: implement custom
	// component
	JScrollPane personSummaryScrollPane = new JScrollPane(personSummary);

	final PersonTree personTree = new PersonTree();
	JScrollPane personTreeScrollPane = new JScrollPane(personTree);

	final JComponent personWheel = new JPanel(); // TODO implement a custom
	// component
	JScrollPane personWheelScrollPane = new JScrollPane(personWheel);

	final JPanel personRelationsPanel = new JPanel(new CardLayout());

	final String relationsCommandText = "TEXT"; //$NON-NLS-1$
	final String relationsCommandTree = "TREE"; //$NON-NLS-1$
	final String relationsCommandWheel = "WHEEL"; //$NON-NLS-1$

	personRelationsPanel.add(personSummaryScrollPane, relationsCommandText);
	personRelationsPanel.add(personTreeScrollPane, relationsCommandTree);
	personRelationsPanel.add(personWheelScrollPane, relationsCommandWheel);

	((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, relationsCommandText);

	personRelationsTextButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		personRelationsTextButton.setEnabled(false);
		personRelationsTreeButton.setEnabled(true);
		personRelationsWheelButton.setEnabled(true);

		((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, relationsCommandText);

	    }
	});

	personRelationsTreeButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		personRelationsTextButton.setEnabled(true);
		personRelationsTreeButton.setEnabled(false);
		personRelationsWheelButton.setEnabled(true);

		CardLayout cl = (CardLayout) personRelationsPanel.getLayout();
		cl.show(personRelationsPanel, relationsCommandTree);

	    }
	});

	personRelationsWheelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		personRelationsTextButton.setEnabled(true);
		personRelationsTreeButton.setEnabled(true);
		personRelationsWheelButton.setEnabled(false);

		((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, relationsCommandWheel);

	    }
	});
	personRelationsZoomInButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    personTree.setGenerations(personTree.getGenerations() + 1);
		    // personWheel.setGenerations(personWheel.getGenerations() +
		    // 1);
		    personRelationsZoomOutButton.setEnabled(true);
		} catch (Exception ex) {
		    throw new RuntimeException(ex);
		}
	    }
	});

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
	PersonEditor personEditorCard = new PersonEditor();

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
	setDefaultCloseOperation(EXIT_ON_CLOSE);

	// Connect the button to the Cards
	prevPersonViewBtn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		((CardLayout) personViewPanel.getLayout()).previous(personViewPanel);

	    }
	});

	nextPersonViewBtn.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		((CardLayout) personViewPanel.getLayout()).next(personViewPanel);
	    }
	});

	// Disable Remove and prev/next card when nothing is selected in list
	personList.addListSelectionListener(new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		boolean bSelectionExists = !personList.isSelectionEmpty();
		prevPersonViewBtn.setEnabled(bSelectionExists);
		nextPersonViewBtn.setEnabled(bSelectionExists);
		remPersonBtn.setEnabled(bSelectionExists);

		personTree.setCenter(personList.getSelectedValue());
	    }
	});

	personRelationsPrintButton.addActionListener(new ActionListener() {

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
	});
    }
}

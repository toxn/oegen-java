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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import data.Person;

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

	JButton addPersonBtn = new JButton("+");
	personListButtons.add(addPersonBtn);

	final JButton remPersonBtn = new JButton("×");
	remPersonBtn.setEnabled(false);
	personListButtons.add(remPersonBtn);

	JButton searchPersonBtn = new JButton("Search");
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

	final JButton personRelationsTextButton = new JButton("Text");
	personRelationsTextButton.setEnabled(false);
	personRelationsButtons.add(personRelationsTextButton, Box.LEFT_ALIGNMENT);

	final JButton personRelationsTreeButton = new JButton("Tree");
	personRelationsTreeButton.setEnabled(true);
	personRelationsButtons.add(personRelationsTreeButton, Box.LEFT_ALIGNMENT);

	final JButton personRelationsWheelButton = new JButton("Wheel");
	personRelationsWheelButton.setEnabled(true);
	personRelationsButtons.add(personRelationsWheelButton, Box.LEFT_ALIGNMENT);

	// personRelationsButtons.add(Box.createHorizontalStrut(20));

	final JButton personRelationsZoomInButton = new JButton("+");
	personRelationsZoomInButton.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomInButton, Box.RIGHT_ALIGNMENT);

	final JButton personRelationsZoomOutButton = new JButton("-");
	personRelationsZoomOutButton.setEnabled(true);
	personRelationsButtons.add(personRelationsZoomOutButton, Box.RIGHT_ALIGNMENT);

	JButton personRelationsRotateLeftButton = new JButton("<-\\");
	personRelationsRotateLeftButton.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateLeftButton, Box.RIGHT_ALIGNMENT);

	JButton personRelationsRotateRightButton = new JButton("/->");
	personRelationsRotateRightButton.setEnabled(true);
	personRelationsButtons.add(personRelationsRotateRightButton, Box.RIGHT_ALIGNMENT);

	JButton personRelationsPrintButton = new JButton("Print");
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

	final String relationsCommandText = "TEXT";
	final String relationsCommandTree = "TREE";
	final String relationsCommandWheel = "WHEEL";

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
		cl.show(personRelationsPanel, "TREE");

	    }
	});

	personRelationsWheelButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		personRelationsTextButton.setEnabled(true);
		personRelationsTreeButton.setEnabled(true);
		personRelationsWheelButton.setEnabled(false);

		((CardLayout) personRelationsPanel.getLayout()).show(personRelationsPanel, "WHEEL");

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
		    throw new RuntimeException();
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
		    throw new RuntimeException();
		}

	    }
	});

	personRelationsCard.add(personRelationsPanel);
	/**
	 * Enable fine editing of person's details such as name, events and
	 * relations
	 */
	JComponent personEditorCard = Box.createVerticalBox();

	/**
	 * The type of person view (3 types : list, graphical, details)
	 */
	Box personViewPane = Box.createHorizontalBox();

	/**
	 * A button that switchs from person Details to person graph, or from
	 * person graph to person list
	 */
	final JButton prevPersonViewBtn = new JButton("<");
	prevPersonViewBtn.setEnabled(false);
	personViewPane.add(prevPersonViewBtn);

	/**
	 * The container that displays list, graph or detail view
	 */
	final JPanel personViewPanel = new JPanel(new CardLayout());
	personViewPanel.add(personListCard, "LIST");
	personViewPanel.add(personRelationsCard, "GRAPH");
	personViewPanel.add(personEditorCard, "DETAILS");
	personViewPane.add(personViewPanel);

	/**
	 * A button than switches from person list to person graph or from
	 * person graph to person details
	 */
	final JButton nextPersonViewBtn = new JButton(">");
	nextPersonViewBtn.setEnabled(false);
	personViewPane.add(nextPersonViewBtn);

	/**
	 * This permits to select type of object the user wants to work with
	 * (Person, Place, Source, etc)
	 *
	 */
	JTabbedPane objectPane = new JTabbedPane(JTabbedPane.LEFT);
	objectPane.addTab("Person", personViewPane);

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

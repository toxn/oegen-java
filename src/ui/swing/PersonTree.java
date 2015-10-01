/**
 * Displays a tree of ascendant and/or descendants of a person
 */
package ui.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.border.Border;

import data.Person;

/**
 * @author toxn
 *
 */
public final class PersonTree
extends PersonRelationalGraphic
{
    private class PersonTreeData {
	public int maxDGen = -1;
	public int maxAGen = -1;
	public int dWeight = -1;
	Vector<Person> descendants = new Vector<Person>();
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final Hashtable<Person, PersonTreeData> personDict = new Hashtable<Person, PersonTreeData>();
    private final int boxWidth = 180;
    private final int boxHeight = 40;
    private final int boxSpace = 13;
    private final int boxStrokeWidth = 2;
    private final int lineWidth = 1;
    private final int fontSize = 8;
    private final int boxMargin = 2;

    private final int margin = 5;

    private final BasicStroke boxStroke = new BasicStroke(boxStrokeWidth);
    private final BasicStroke lineStroke = new BasicStroke(lineWidth);

    /**
     * Number of descendant generations to display
     */
    private int genAsc;

    /**
     * Number of descendant generations to display
     */
    private int genDesc;

    /**
     * Calculate the layout weight of the descendants
     * @param person Person from which descendants are taken from
     * @return layout vertical weight (ie # of vertical boxes equivalent)
     */
    private int calcDWeight(Person person) {
	PersonTreeData ptd = personDict.get(person);

	if (ptd.descendants.size() == 0) {
	    ptd.dWeight = 1;
	    return ptd.dWeight;
	}

	int dWeight = 0;
	for (Person descendant : ptd.descendants) {
	    dWeight += calcDWeight(descendant);
	}

	ptd.dWeight = dWeight;
	return dWeight;
    }

    private int countAscGen(Person p) {
	int maxAGen = 0;

	Person father = p.getFather();
	Person mother = p.getMother();

	if(father != null) {
	    int fatherMaxAGen = countAscGen(father) + 1;
	    if(fatherMaxAGen > maxAGen) {
		maxAGen = fatherMaxAGen;
	    }
	}

	if(mother != null) {
	    int motherMaxAGen = countAscGen(mother) + 1;
	    if(motherMaxAGen > maxAGen) {
		maxAGen = motherMaxAGen;
	    }
	}

	return maxAGen;
    }

    /**
     * Counts the number of descendants for each generation.
     * @param person The Person from which descdendants are searched for
     * @param generation the number of the generation person is part of
     * @param boxPerGen a hashtable containing generation/count pairs
     */
    private int countDescdendant(Person person, int maxDGen) {
	if (maxDGen == 0)
	    return 0;

	int count = 0;

	for (Person descendant : Person.Persons) {
	    if(descendant.getFather() == person || descendant.getMother() == person) {
		int countDesc = countDescdendant(descendant, maxDGen - 1);

		count += countDesc > 0 ? countDesc : 1;
	    }
	}

	return count;
    }

    private int countDescGen(Person p) {
	int count = 0;

	for(Person descendant : personDict.get(p).descendants) {
	    int countd = countDescGen(descendant)+1;

	    if(countd > count) {
		count = countd;
	    }
	}

	return count;
    }

    private void drawAncestorsLeftToRight(Person person, int generations, Graphics2D g2d, int x, int y) {
	if (person == null || g2d == null || generations == 0)
	    return;

	Person father = person.getFather();
	Person mother = person.getMother();

	if (father == null && mother == null)
	    return;

	// Change graphic context
	Stroke oldStroke = g2d.getStroke();
	Color oldColor = g2d.getColor();

	g2d.setStroke(lineStroke);
	g2d.setColor(Color.black);

	/* FIXME overflows if generations > 30 or 62 */
	int maxVertBoxesPerAncestor = generations == 1 ? 1 : 2 << generations - 2;
	int x1 = x + boxWidth;
	int y1 = y + boxHeight / 2;
	int x2 = x1 + boxSpace / 2;
	int y2 = y1;

	g2d.drawLine(x1, y1, x2, y2);

	x1 = x2;
	x2 += boxSpace / 2;
	int vertOffset = maxVertBoxesPerAncestor * (boxHeight + boxSpace) / 2;
	y1 = y1 - vertOffset;

	if (father != null) {
	    // Link to father's box
	    g2d.drawLine(x1, y1, x1, y2);
	    g2d.drawLine(x1, y1, x2, y1);

	    // Draw the father's box
	    drawBox(father, g2d, x2, y1 - boxHeight / 2);
	    drawAncestorsLeftToRight(father, generations - 1, g2d, x2, y1 - boxHeight / 2);
	}

	y1 = y2;
	y2 = y2 + vertOffset;

	if (mother != null) {
	    // Link to mother's box
	    g2d.drawLine(x1, y1, x1, y2);
	    g2d.drawLine(x1, y2, x2, y2);

	    // Draw the mother's box
	    drawBox(mother, g2d, x2, y2 - boxHeight / 2);
	    drawAncestorsLeftToRight(mother, generations - 1, g2d, x2, y2 - boxHeight / 2);
	}

	// Restore graphic context
	g2d.setStroke(oldStroke);
	g2d.setColor(oldColor);
    }

    private void drawBox(Person person, Graphics2D g2d, int x, int y) {
	if (person == null || g2d == null)
	    return;

	// Save graphic context
	Stroke oldStroke = g2d.getStroke();
	Color oldColor = g2d.getColor();

	g2d.setStroke(boxStroke);
	g2d.setColor(Color.white);
	g2d.fillRect(x, y, boxWidth, boxHeight);
	g2d.setColor(Color.black);
	g2d.drawRect(x, y, boxWidth, boxHeight);
	g2d.setFont(getFont().deriveFont(fontSize));
	g2d.drawString(
		person.getLastName() + ", " + person.getFirstName() + " (" + personDict.get(person).dWeight + ")",
		x + boxMargin + boxStrokeWidth,
		y + fontSize + boxMargin + boxStrokeWidth);

	// Restore graphic context
	g2d.setStroke(oldStroke);
	g2d.setColor(oldColor);
    }

    private void drawDescendantsLeftToRight(Person person, int generations, Graphics2D g2d, int x, int y) {
	if (person == null || generations == 0 || g2d == null)
	    return;

	// Change graphic context
	Stroke oldStroke = g2d.getStroke();
	Color oldColor = g2d.getColor();

	g2d.setStroke(lineStroke);
	g2d.setColor(Color.black);

	y += boxHeight / 2;

	int x1 = x;
	int x2 = x - boxSpace / 2;

	g2d.drawLine(x1, y, x2, y);

	int dWeight = personDict.get(person).dWeight;
	int vOffset = (dWeight - 1) * (boxHeight + boxSpace) / 2;
	int y1 = y - vOffset;
	int y2 = y + vOffset;

	g2d.drawLine(x2, y1, x2, y2);

	x1 = x2 - boxSpace / 2;
	int x3 = x1 - boxWidth;

	for (Person descendant : personDict.get(person).descendants) {
	    g2d.drawLine(x1, y1, x2, y1);

	    y2 = y1 - boxHeight / 2;
	    drawBox(descendant, g2d, x3, y2);
	    drawDescendantsLeftToRight(descendant, generations - 1, g2d, x3, y2);

	    y1 += personDict.get(descendant).dWeight * (boxHeight + boxSpace);
	}

	// Restore graphic context
	g2d.setStroke(oldStroke);
	g2d.setColor(oldColor);
    }

    private void drawLeftToRight(Graphics2D g2d) {
	if (g2d != null) {
	    try {
		if (getCenter() == null)
		    return;

		Border border = getBorder();
		Insets insets;

		if (border == null) {
		    insets = new Insets(0, 0, 0, 0);
		} else {
		    insets = border.getBorderInsets(this);
		}

		int startx = insets.left + margin + genDesc * (boxWidth + boxSpace);
		int starty = insets.top + (getHeight() - insets.top - insets.bottom - boxHeight) / 2;

		drawBox(getCenter(), g2d, startx, starty);
		drawAncestorsLeftToRight(getCenter(), genAsc - 1, g2d, startx, starty);
		drawDescendantsLeftToRight(getCenter(), genDesc, g2d, startx, starty);

	    } finally {
		g2d.dispose(); // clean up
	    }
	}
    }

    @Override
    protected void paintComponent(Graphics g) {
	if (isOpaque()) { // paint background
	    g.setColor(getBackground());

	    Border border = getBorder();
	    Insets insets;

	    if (border == null) {
		insets = new Insets(0, 0, 0, 0);
	    } else {
		insets = border.getBorderInsets(this);
	    }

	    g.fillRect(insets.left, insets.top, getWidth() - insets.left - insets.right,
		    getHeight() - insets.top - insets.bottom);
	}

	Graphics2D g2d = (Graphics2D) g.create();
	drawLeftToRight(g2d);
    }

    @Override
    protected void rebuild() {
	// First let's populate the person data dictionary
	personDict.clear();

	for (Person p : Person.Persons) {
	    PersonTreeData ptd = new PersonTreeData();
	    personDict.put(p, ptd);

	    ptd.maxAGen = countAscGen(p);

	    for (Person p2 : Person.Persons) {
		if (p2.getFather() == p || p2.getMother() == p) {
		    ptd.descendants.add(p2);
		}
	    }
	}

	for (Person p : personDict.keySet()) {
	    personDict.get(p).maxDGen = countDescGen(p);

	    PersonTreeData ptd = personDict.get(p);
	    if (ptd.dWeight == -1) {
		ptd.dWeight = calcDWeight(p);
	    }
	}

	/**
	 * Maximum number of ascendants boxes for the same generation.
	 *
	 * For a n generations diagram, equals 2^(n-1)
	 */
	int maxAGen = personDict.get(getCenter()).maxAGen;
	int maxDGen = personDict.get(getCenter()).maxDGen;
	genAsc = maxAGen + 1 > getGenerations() ? getGenerations() : maxAGen + 1;
	genDesc = maxDGen > getGenerations() - 1 ? getGenerations() - 1 : maxDGen;
	int maxABox = genAsc == 1 ? 1 : 2 << genAsc - 2;

	/**
	 * Maximum number of descdendant boxes for the same generation.
	 */
	int maxDBox = countDescdendant(getCenter(), maxDGen);

	int maxBox = maxABox > maxDBox ? maxABox : maxDBox;

	Border border = getBorder();
	Insets insets;

	if (border == null) {
	    insets = new Insets(0, 0, 0, 0);
	} else {
	    insets = border.getBorderInsets(this);
	}

	setPreferredSize(new Dimension(
		insets.left + 2 * margin + (genAsc + genDesc) * boxWidth + (genAsc + genDesc - 1) * boxSpace
		+ insets.right,
		insets.top + 2 * margin + maxBox * boxHeight + (maxBox - 1) * boxSpace + insets.bottom));

	// Forces parent ScrollPane to lay out its scrollbars
	revalidate();

	// Repaints PersonTree even if it's size is smaller than before
	repaint();
    }

}

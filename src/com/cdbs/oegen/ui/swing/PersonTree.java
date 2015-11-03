/**
 * Displays a tree of ascendant and/or descendants of a person
 */
package com.cdbs.oegen.ui.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Hashtable;

import javax.swing.border.Border;

import com.cdbs.oegen.data.Person;

/**
 * @author toxn
 *
 */
public final class PersonTree
extends PersonRelationalGraphic
implements Printable
{
    private class PersonTreeData {
	public int maxDGen = -1;
	public int maxAGen = -1;
	public int dWeight = -1;
	public PersonTreeData() {
	    // TODO Stub du constructeur généré automatiquement
	}
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // TODO: intergrate that in class Person
    private final Hashtable<Person, PersonTreeData> personDict = new Hashtable<Person, PersonTreeData>();
    /**
     * Default width of a person box (pixels)
     */
    private int defBoxWidth = 180;

    /**
     * Default height of a person box (pixels)
     */
    private int defBoxHeight = 40;

    /**
     * Default spacing between boxes (pixels)
     */
    private int defBoxSpace = 20;
    private int defMariageSpace = defBoxSpace;

    /**
     * Default box rectangle width (pixels)
     */
    private final int defBoxStrokeWidth = 2;

    /**
     * Default width of line connecting boxes (pixels)
     */
    private final int defLineWidth = 1;

    /**
     * Default margins inside boxes (pixels)
     */
    private final int defBoxMargin = 1;

    /**
     * Default margins around the whole graphic
     */
    private final int defMargin = 5;

    private final BasicStroke boxStroke = new BasicStroke(defBoxStrokeWidth);
    private final BasicStroke lineStroke = new BasicStroke(defLineWidth);

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

	if (person.children.size() == 0) {
	    ptd.dWeight = 1;
	    return ptd.dWeight;
	}

	int dWeight = 0;
	for (Person descendant : person.children) {
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

	for (Person descendant : Person.persons) {
	    if(descendant.getFather() == person || descendant.getMother() == person) {
		int countDesc = countDescdendant(descendant, maxDGen - 1);

		count += countDesc > 0 ? countDesc : 1;
	    }
	}

	return count;
    }

    private int countDescGen(Person p) {
	int count = 0;

	for (Person descendant : p.children) {
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
	int x1 = x + defBoxWidth;
	int y1 = y + defBoxHeight / 2;
	int x2 = x1 + defBoxSpace / 2;
	int y2 = y1;

	g2d.drawLine(x1, y1, x2, y2);

	x1 = x2;
	x2 += defBoxSpace / 2;
	int vertOffset = maxVertBoxesPerAncestor * (defBoxHeight + defMariageSpace) / 2;
	y1 = y1 - vertOffset;

	if (father != null) {
	    // Link to father's box
	    g2d.drawLine(x1, y1, x1, y2);
	    g2d.drawLine(x1, y1, x2, y1);

	    // Draw the father's box
	    drawBox(father, g2d, x2, y1 - defBoxHeight / 2);
	    drawAncestorsLeftToRight(father, generations - 1, g2d, x2, y1 - defBoxHeight / 2);
	}

	y1 = y2;
	y2 = y2 + vertOffset;

	if (mother != null) {
	    // Link to mother's box
	    g2d.drawLine(x1, y1, x1, y2);
	    g2d.drawLine(x1, y2, x2, y2);

	    // Draw the mother's box
	    drawBox(mother, g2d, x2, y2 - defBoxHeight / 2);
	    drawAncestorsLeftToRight(mother, generations - 1, g2d, x2, y2 - defBoxHeight / 2);
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
	g2d.fillRect(x, y, defBoxWidth, defBoxHeight);
	g2d.setColor(Color.black);
	g2d.drawRect(x, y, defBoxWidth, defBoxHeight);
	g2d.drawString(person.getLastName() + " " + person.getFirstName(), //$NON-NLS-1$
		x + defBoxMargin + defBoxStrokeWidth,
		y + (int) g2d.getFontMetrics().getMaxCharBounds(g2d).getHeight() + defBoxMargin + defBoxStrokeWidth);

	// Restore graphic context
	g2d.setStroke(oldStroke);
	g2d.setColor(oldColor);
    }

    private void drawDescendantsLeftToRight(Person person, int generations, Graphics2D g2d, int x, int y) {
	if (person == null || generations == 0 || g2d == null)
	    return;

	if (person.children.size() == 0)
	    return;

	// Change graphic context
	Stroke oldStroke = g2d.getStroke();
	Color oldColor = g2d.getColor();

	g2d.setStroke(lineStroke);
	g2d.setColor(Color.black);

	int y0 = y + defBoxHeight / 2;

	int x1 = x;
	int x2 = x - defBoxSpace / 2;

	g2d.drawLine(x1, y0, x2, y0);

	int dWeight = personDict.get(person).dWeight;
	int vOffset = dWeight * (defBoxHeight + defBoxSpace) / 2;
	int v1Offset;
	int v2Offset;

	if(dWeight==1) {
	    v1Offset = v2Offset = vOffset;
	}
	else {
	    v1Offset = (dWeight - personDict.get(person.children.firstElement()).dWeight)
		    * (defBoxHeight + defBoxSpace) / 2;
	    v2Offset = (dWeight - personDict.get(person.children.lastElement()).dWeight)
		    * (defBoxHeight + defBoxSpace)
		    / 2;
	}
	int y1 = y0 - v1Offset;
	int y2 = y0 + v2Offset;

	g2d.drawLine(x2, y1, x2, y2);

	x1 = x2 - defBoxSpace / 2;
	int x3 = x1 - defBoxWidth;
	y1 = y1 + v1Offset - vOffset;

	for (Person descendant : person.children) {
	    y1 += personDict.get(descendant).dWeight * (defBoxHeight + defBoxSpace) / 2;
	    g2d.drawLine(x1, y1, x2, y1);

	    y2 = y1 - defBoxHeight / 2;
	    drawBox(descendant, g2d, x3, y2);
	    drawDescendantsLeftToRight(descendant, generations - 1, g2d, x3, y2);

	    y1 += personDict.get(descendant).dWeight * (defBoxHeight + defBoxSpace) / 2;
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

		int startx = insets.left + defMargin + genDesc * (defBoxWidth + defBoxSpace);
		int starty = insets.top + (getHeight() - insets.top - insets.bottom - defBoxHeight) / 2;

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
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
	if (page > 0)
	    return Printable.NO_SUCH_PAGE;

	Graphics2D g2d = (Graphics2D)g;
	g2d.translate(pf.getImageableX(), pf.getImageableY());
	g2d.scale(pf.getWidth() / getWidth(), pf.getHeight() / getHeight());

	drawLeftToRight(g2d);

	return Printable.PAGE_EXISTS;
    }

    @Override
    protected void rebuild() {
	defBoxWidth = (int) getFontMetrics(getFont()).getMaxCharBounds(getGraphics()).getWidth() * 10
		+ 2 * defBoxMargin;
	defBoxHeight = getFontMetrics(getFont()).getHeight() * 4 + 3 * defBoxMargin + defLineWidth;

	defMariageSpace = (int) getFontMetrics(getFont()).getMaxCharBounds(getGraphics()).getWidth() + 2 * defBoxMargin;
	defBoxSpace = defMariageSpace / 2;

	// First let's populate the person com.cdbs.oegen.data dictionary
	personDict.clear();

	for (Person p : Person.persons) {
	    PersonTreeData ptd = new PersonTreeData();
	    personDict.put(p, ptd);
	}

	for (Person p : Person.persons) {
	    PersonTreeData ptd = personDict.get(p);
	    ptd.maxAGen = countAscGen(p);

	    ptd.maxDGen = countDescGen(p);

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
		insets.left + 2 * defMargin + (genAsc + genDesc) * defBoxWidth + (genAsc + genDesc - 1) * defBoxSpace
		+ insets.right,
		insets.top + 2 * defMargin + maxBox * defBoxHeight + (maxBox - 1) * defBoxSpace + insets.bottom));

	// Forces parent ScrollPane to lay out its scrollbars
	revalidate();

	// Repaints PersonTree even if it's size is smaller than before
	repaint();
    }

}

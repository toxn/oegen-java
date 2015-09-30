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

import javax.swing.border.Border;

import data.Person;

/**
 * @author toxn
 *
 */
public final class PersonTree
extends PersonRelationalGraphic
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final int boxWidth = 180;
    private final int boxHeight = 40;
    private final int boxSpace = 20;
    private final int boxStrokeWidth = 2;
    private final int lineWidth = 1;
    private final int fontSize = 8;
    private final int boxMargin = 2;
    private final int margin = 5;

    private final BasicStroke boxStroke = new BasicStroke(boxStrokeWidth);
    private final BasicStroke lineStroke = new BasicStroke(lineWidth);

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
	g2d.drawString(person.getLastName() + ", " + person.getFirstName(), x + boxMargin + boxStrokeWidth,
		y + fontSize + boxMargin + boxStrokeWidth);

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

		int startx = insets.left + margin;
		int starty = insets.top + (getHeight() - insets.top - insets.bottom - boxHeight) / 2;

		drawBox(getCenter(), g2d, startx, starty);
		drawAncestorsLeftToRight(getCenter(), getGenerations() - 1, g2d, startx, starty);

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
	// Fixed 4 generation horizontal layout
	int maxVerticalBoxesNumber = getGenerations() == 1 ? 1 : 2 << getGenerations() - 2;

	Border border = getBorder();
	Insets insets;

	if (border == null) {
	    insets = new Insets(0, 0, 0, 0);
	} else {
	    insets = border.getBorderInsets(this);
	}

	setPreferredSize(new Dimension(
		insets.left + 2 * margin + getGenerations() * boxWidth + (getGenerations() - 1) * boxSpace
		+ insets.right,
		insets.top + 2 * margin + maxVerticalBoxesNumber * boxHeight + (maxVerticalBoxesNumber - 1) * boxSpace
		+ insets.bottom));

	// Forces parent ScrollPane to lay out its scrollbars
	revalidate();

	// Repaints PersonTree even if it's size is smaller than before
	repaint();
    }


}

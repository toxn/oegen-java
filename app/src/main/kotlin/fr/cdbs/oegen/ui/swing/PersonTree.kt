package fr.cdbs.oegen.ui.swing

import fr.cdbs.oegen.data.Person
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Stroke
import java.awt.print.PageFormat
import java.awt.print.Printable
import java.awt.print.PrinterException
import javax.swing.border.Border
import kotlin.math.max

class PersonTree : PersonRelationalGraphic(), Printable {
    private class PersonTreeData {
        var maxDGen: Int = -1
        var maxAGen: Int = -1
        var dWeight: Int = -1
    }

    private val personDict = HashMap<Person, PersonTreeData>()
    private var defBoxWidth = 180
    private var defBoxHeight = 40
    private var defBoxSpace = 20
    private var defMariageSpace = defBoxSpace
    private val defBoxStrokeWidth = 2
    private val defLineWidth = 1
    private val defBoxMargin = 1
    private val defMargin = 5
    private val boxStroke = BasicStroke(defBoxStrokeWidth.toFloat())
    private val lineStroke = BasicStroke(defLineWidth.toFloat())
    private var genAsc = 0
    private var genDesc = 0

    private fun calcDWeight(person: Person): Int {
        val ptd = personDict.getValue(person)

        if (person.children.size == 0) {
            ptd.dWeight = 1
            return ptd.dWeight
        }

        var dWeight = 0
        for (descendant in person.children) {
            dWeight += calcDWeight(descendant!!)
        }

        ptd.dWeight = dWeight
        return dWeight
    }

    private fun countAscGen(p: Person): Int {
        var maxAGen = 0

        val father = p.father
        val mother = p.mother

        if (father != null) {
            val fatherMaxAGen = countAscGen(father) + 1
            if (fatherMaxAGen > maxAGen) {
                maxAGen = fatherMaxAGen
            }
        }

        if (mother != null) {
            val motherMaxAGen = countAscGen(mother) + 1
            if (motherMaxAGen > maxAGen) {
                maxAGen = motherMaxAGen
            }
        }

        return maxAGen
    }

    private fun countDescdendant(person: Person, maxDGen: Int): Int {
        if (maxDGen == 0) return 0

        var count = 0

        for (descendant in Person.persons) {
            if (descendant.father === person || descendant.mother === person) {
                val countDesc = countDescdendant(descendant, maxDGen - 1)
                count += if (countDesc > 0) countDesc else 1
            }
        }

        return count
    }

    private fun countDescGen(p: Person): Int {
        var count = 0

        for (descendant in p.children) {
            val countd = countDescGen(descendant!!) + 1
            if (countd > count) {
                count = countd
            }
        }

        return count
    }

    private fun drawAncestorsLeftToRight(person: Person?, generations: Int, g2d: Graphics2D, x: Int, y: Int) {
        if (person == null || generations == 0) return

        val father = person.father
        val mother = person.mother

        if (father == null && mother == null) return

        val oldStroke = g2d.stroke
        val oldColor = g2d.color

        g2d.stroke = lineStroke
        g2d.color = Color.BLACK

        val maxVertBoxesPerAncestor = if (generations == 1) 1 else 2 shl generations - 2
        var x1 = x + defBoxWidth
        var y1 = y + defBoxHeight / 2
        var x2 = x1 + defBoxSpace / 2
        val y2 = y1

        g2d.drawLine(x1, y1, x2, y2)

        x1 = x2
        x2 += defBoxSpace / 2
        val vertOffset = maxVertBoxesPerAncestor * (defBoxHeight + defMariageSpace) / 2
        y1 -= vertOffset

        if (father != null) {
            g2d.drawLine(x1, y1, x1, y2)
            g2d.drawLine(x1, y1, x2, y1)
            drawBox(father, g2d, x2, y1 - defBoxHeight / 2)
            drawAncestorsLeftToRight(father, generations - 1, g2d, x2, y1 - defBoxHeight / 2)
        }

        y1 = y2
        val yBottom = y2 + vertOffset

        if (mother != null) {
            g2d.drawLine(x1, y1, x1, yBottom)
            g2d.drawLine(x1, yBottom, x2, yBottom)
            drawBox(mother, g2d, x2, yBottom - defBoxHeight / 2)
            drawAncestorsLeftToRight(mother, generations - 1, g2d, x2, yBottom - defBoxHeight / 2)
        }

        g2d.stroke = oldStroke
        g2d.color = oldColor
    }

    private fun drawBox(person: Person, g2d: Graphics2D, x: Int, y: Int) {
        val oldStroke = g2d.stroke
        val oldColor = g2d.color

        g2d.stroke = boxStroke
        g2d.color = Color.WHITE
        g2d.fillRect(x, y, defBoxWidth, defBoxHeight)
        g2d.color = Color.BLACK
        g2d.drawRect(x, y, defBoxWidth, defBoxHeight)
        g2d.drawString(
            person.lastName + " " + person.firstName,
            x + defBoxMargin + defBoxStrokeWidth,
            y + g2d.fontMetrics.getMaxCharBounds(g2d).height.toInt() + defBoxMargin + defBoxStrokeWidth,
        )

        g2d.stroke = oldStroke
        g2d.color = oldColor
    }

    private fun drawDescendantsLeftToRight(person: Person, generations: Int, g2d: Graphics2D, x: Int, y: Int) {
        if (generations == 0 || person.children.size == 0) return

        val oldStroke = g2d.stroke
        val oldColor = g2d.color

        g2d.stroke = lineStroke
        g2d.color = Color.BLACK

        val y0 = y + defBoxHeight / 2
        val x1 = x
        var x2 = x - defBoxSpace / 2

        g2d.drawLine(x1, y0, x2, y0)

        val dWeight = personDict.getValue(person).dWeight
        val vOffset = dWeight * (defBoxHeight + defBoxSpace) / 2
        val v1Offset: Int
        val v2Offset: Int

        if (dWeight == 1) {
            v1Offset = vOffset
            v2Offset = vOffset
        } else {
            val firstChild = person.children.getElementAt(0)!!
            val lastChild = person.children.getElementAt(person.children.size - 1)!!
            v1Offset = (dWeight - personDict.getValue(firstChild).dWeight) * (defBoxHeight + defBoxSpace) / 2
            v2Offset = (dWeight - personDict.getValue(lastChild).dWeight) * (defBoxHeight + defBoxSpace) / 2
        }

        var y1 = y0 - v1Offset
        val yBottom = y0 + v2Offset

        g2d.drawLine(x2, y1, x2, yBottom)

        val xMid = x2 - defBoxSpace / 2
        val x3 = xMid - defBoxWidth
        y1 = y1 + v1Offset - vOffset

        for (descendant in person.children) {
            val child = descendant!!
            y1 += personDict.getValue(child).dWeight * (defBoxHeight + defBoxSpace) / 2
            g2d.drawLine(xMid, y1, x2, y1)

            val yBox = y1 - defBoxHeight / 2
            drawBox(child, g2d, x3, yBox)
            drawDescendantsLeftToRight(child, generations - 1, g2d, x3, yBox)

            y1 += personDict.getValue(child).dWeight * (defBoxHeight + defBoxSpace) / 2
        }

        g2d.stroke = oldStroke
        g2d.color = oldColor
    }

    private fun drawLeftToRight(g2d: Graphics2D) {
        try {
            val treeCenter = center ?: return

            val border: Border? = border
            val insets = border?.getBorderInsets(this) ?: java.awt.Insets(0, 0, 0, 0)

            val startx = insets.left + defMargin + genDesc * (defBoxWidth + defBoxSpace)
            val starty = insets.top + (height - insets.top - insets.bottom - defBoxHeight) / 2

            drawBox(treeCenter, g2d, startx, starty)
            drawAncestorsLeftToRight(treeCenter, genAsc - 1, g2d, startx, starty)
            drawDescendantsLeftToRight(treeCenter, genDesc, g2d, startx, starty)
        } finally {
            g2d.dispose()
        }
    }

    override fun paintComponent(g: Graphics) {
        if (isOpaque) {
            g.color = background
            val border: Border? = border
            val insets = border?.getBorderInsets(this) ?: java.awt.Insets(0, 0, 0, 0)
            g.fillRect(
                insets.left,
                insets.top,
                width - insets.left - insets.right,
                height - insets.top - insets.bottom,
            )
        }

        val g2d = g.create() as Graphics2D
        drawLeftToRight(g2d)
    }

    override fun print(g: Graphics, pf: PageFormat, page: Int): Int {
        if (page > 0) return Printable.NO_SUCH_PAGE

        val g2d = g.create() as Graphics2D
        g2d.translate(pf.imageableX, pf.imageableY)
        g2d.scale(pf.width / width, pf.height / height)
        drawLeftToRight(g2d)

        return Printable.PAGE_EXISTS
    }

    override fun rebuild() {
        val treeCenter = center
        if (treeCenter == null) {
            personDict.clear()
            preferredSize = Dimension(0, 0)
            revalidate()
            repaint()
            return
        }

        defBoxWidth = getFontMetrics(font).getMaxCharBounds(graphics).width.toInt() * 10 + 2 * defBoxMargin
        defBoxHeight = getFontMetrics(font).height * 4 + 3 * defBoxMargin + defLineWidth
        defMariageSpace = getFontMetrics(font).getMaxCharBounds(graphics).width.toInt() + 2 * defBoxMargin
        defBoxSpace = defMariageSpace / 2

        personDict.clear()

        for (p in Person.persons) {
            personDict[p] = PersonTreeData()
        }

        for (p in Person.persons) {
            val ptd = personDict.getValue(p)
            ptd.maxAGen = countAscGen(p)
            ptd.maxDGen = countDescGen(p)
            if (ptd.dWeight == -1) {
                ptd.dWeight = calcDWeight(p)
            }
        }

        val centerData = personDict.getValue(treeCenter)
        val maxAGen = centerData.maxAGen
        val maxDGen = centerData.maxDGen
        genAsc = if (maxAGen + 1 > generations) generations else maxAGen + 1
        genDesc = if (maxDGen > generations - 1) generations - 1 else maxDGen
        val maxABox = if (genAsc == 1) 1 else 2 shl genAsc - 2
        val maxDBox = countDescdendant(treeCenter, maxDGen)
        val maxBox = max(maxABox, maxDBox)

        val border: Border? = border
        val insets = border?.getBorderInsets(this) ?: java.awt.Insets(0, 0, 0, 0)

        preferredSize = Dimension(
            insets.left + 2 * defMargin + (genAsc + genDesc) * defBoxWidth + (genAsc + genDesc - 1) * defBoxSpace + insets.right,
            insets.top + 2 * defMargin + maxBox * defBoxHeight + (maxBox - 1) * defBoxSpace + insets.bottom,
        )

        revalidate()
        repaint()
    }
}

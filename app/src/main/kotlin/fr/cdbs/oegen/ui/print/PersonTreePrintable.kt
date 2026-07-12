package fr.cdbs.oegen.ui.print

import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.compose.PersonTreeLayoutEngine
import fr.cdbs.oegen.ui.compose.TreeMetrics
import java.awt.Color
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.print.PageFormat
import java.awt.print.Printable
import java.awt.print.PrinterException

class PersonTreePrintable(
    private val center: Person,
    private val generations: Int,
) : Printable {
    private val layout by lazy {
        val metrics = TreeMetrics(
            boxWidth = 120f,
            boxHeight = 36f,
            boxSpace = 10f,
            marriageSpace = 20f,
        )
        PersonTreeLayoutEngine.compute(center, generations, metrics)
    }

    override fun print(graphics: Graphics, pageFormat: PageFormat, pageIndex: Int): Int {
        if (pageIndex > 0) return Printable.NO_SUCH_PAGE

        val g2 = graphics as Graphics2D
        g2.translate(pageFormat.imageableX, pageFormat.imageableY)

        val scaleX = pageFormat.imageableWidth / layout.width.toDouble()
        val scaleY = pageFormat.imageableHeight / layout.height.toDouble()
        val scale = minOf(scaleX, scaleY, 1.0)
        g2.scale(scale, scale)

        g2.color = Color.BLACK
        for (line in layout.lines) {
            g2.drawLine(line.x1.toInt(), line.y1.toInt(), line.x2.toInt(), line.y2.toInt())
        }

        val font = Font(Font.SANS_SERIF, Font.PLAIN, 10)
        g2.font = font
        val metrics: FontMetrics = g2.fontMetrics

        for (box in layout.boxes) {
            g2.color = if (box.person === center) Color.LIGHT_GRAY else Color.WHITE
            g2.fillRect(box.left.toInt(), box.top.toInt(), box.width.toInt(), box.height.toInt())
            g2.color = Color.BLACK
            g2.drawRect(box.left.toInt(), box.top.toInt(), box.width.toInt(), box.height.toInt())

            val label = PersonTreeLayoutEngine.boxLabel(box.person)
            g2.drawString(
                label,
                box.left.toInt() + 2,
                box.top.toInt() + metrics.ascent + 2,
            )
        }

        return Printable.PAGE_EXISTS
    }
}

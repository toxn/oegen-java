package fr.cdbs.oegen.ui.compose

import fr.cdbs.oegen.data.Person
import kotlin.math.max

data class TreeBox(
    val person: Person,
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float,
)

data class TreeLine(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
)

data class PersonTreeLayout(
    val boxes: List<TreeBox>,
    val lines: List<TreeLine>,
    val width: Float,
    val height: Float,
)

data class TreeMetrics(
    val boxWidth: Float,
    val boxHeight: Float,
    val boxSpace: Float,
    val marriageSpace: Float,
    val boxMargin: Float = 1f,
    val margin: Float = 5f,
)

object PersonTreeLayoutEngine {
    private class PersonTreeData {
        var maxDGen: Int = -1
        var maxAGen: Int = -1
        var dWeight: Int = -1
    }

    fun compute(center: Person, generations: Int, metrics: TreeMetrics): PersonTreeLayout {
        require(generations >= 1)

        val personDict = HashMap<Person, PersonTreeData>()
        for (p in Person.persons) {
            personDict[p] = PersonTreeData()
        }

        for (p in Person.persons) {
            val ptd = personDict.getValue(p)
            ptd.maxAGen = countAscGen(p)
            ptd.maxDGen = countDescGen(p)
            if (ptd.dWeight == -1) {
                ptd.dWeight = calcDWeight(p, personDict)
            }
        }

        val centerData = personDict.getValue(center)
        val maxAGen = centerData.maxAGen
        val maxDGen = centerData.maxDGen
        val genAsc = if (maxAGen + 1 > generations) generations else maxAGen + 1
        val genDesc = if (maxDGen > generations - 1) generations - 1 else maxDGen
        val maxABox = if (genAsc == 1) 1 else 2 shl genAsc - 2
        val maxDBox = countDescendant(center, maxDGen)
        val maxBox = max(maxABox, maxDBox)

        val width = 2 * metrics.margin + (genAsc + genDesc) * metrics.boxWidth +
            (genAsc + genDesc - 1) * metrics.boxSpace
        val height = 2 * metrics.margin + maxBox * metrics.boxHeight +
            (maxBox - 1) * metrics.boxSpace

        val boxes = mutableListOf<TreeBox>()
        val lines = mutableListOf<TreeLine>()

        val startX = metrics.margin + genDesc * (metrics.boxWidth + metrics.boxSpace)
        val startY = metrics.margin + (height - 2 * metrics.margin - metrics.boxHeight) / 2f

        addBox(center, startX, startY, metrics, boxes)
        layoutAncestors(center, genAsc - 1, startX, startY, metrics, personDict, boxes, lines)
        layoutDescendants(center, genDesc, startX, startY, metrics, personDict, boxes, lines)

        return PersonTreeLayout(boxes, lines, width, height)
    }

    private fun calcDWeight(person: Person, personDict: Map<Person, PersonTreeData>): Int {
        val ptd = personDict.getValue(person)
        if (person.children.isEmpty()) {
            ptd.dWeight = 1
            return ptd.dWeight
        }

        var dWeight = 0
        for (descendant in person.children) {
            dWeight += calcDWeight(descendant!!, personDict)
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

    private fun countDescendant(person: Person, maxDGen: Int): Int {
        if (maxDGen == 0) return 0

        var count = 0
        for (descendant in Person.persons) {
            if (descendant.father === person || descendant.mother === person) {
                val countDesc = countDescendant(descendant, maxDGen - 1)
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

    private fun addBox(person: Person, x: Float, y: Float, metrics: TreeMetrics, boxes: MutableList<TreeBox>) {
        boxes.add(TreeBox(person, x, y, metrics.boxWidth, metrics.boxHeight))
    }

    private fun addLine(x1: Float, y1: Float, x2: Float, y2: Float, lines: MutableList<TreeLine>) {
        lines.add(TreeLine(x1, y1, x2, y2))
    }

    private fun layoutAncestors(
        person: Person?,
        generations: Int,
        x: Float,
        y: Float,
        metrics: TreeMetrics,
        personDict: Map<Person, PersonTreeData>,
        boxes: MutableList<TreeBox>,
        lines: MutableList<TreeLine>,
    ) {
        if (person == null || generations == 0) return

        val father = person.father
        val mother = person.mother
        if (father == null && mother == null) return

        val maxVertBoxesPerAncestor = if (generations == 1) 1 else 2 shl generations - 2
        var x1 = x + metrics.boxWidth
        var y1 = y + metrics.boxHeight / 2f
        var x2 = x1 + metrics.boxSpace / 2f
        val y2 = y1

        addLine(x1, y1, x2, y2, lines)

        x1 = x2
        x2 += metrics.boxSpace / 2f
        val vertOffset = maxVertBoxesPerAncestor * (metrics.boxHeight + metrics.marriageSpace) / 2f
        y1 -= vertOffset

        if (father != null) {
            addLine(x1, y1, x1, y2, lines)
            addLine(x1, y1, x2, y1, lines)
            addBox(father, x2, y1 - metrics.boxHeight / 2f, metrics, boxes)
            layoutAncestors(father, generations - 1, x2, y1 - metrics.boxHeight / 2f, metrics, personDict, boxes, lines)
        }

        y1 = y2
        val yBottom = y2 + vertOffset

        if (mother != null) {
            addLine(x1, y1, x1, yBottom, lines)
            addLine(x1, yBottom, x2, yBottom, lines)
            addBox(mother, x2, yBottom - metrics.boxHeight / 2f, metrics, boxes)
            layoutAncestors(mother, generations - 1, x2, yBottom - metrics.boxHeight / 2f, metrics, personDict, boxes, lines)
        }
    }

    private fun layoutDescendants(
        person: Person,
        generations: Int,
        x: Float,
        y: Float,
        metrics: TreeMetrics,
        personDict: Map<Person, PersonTreeData>,
        boxes: MutableList<TreeBox>,
        lines: MutableList<TreeLine>,
    ) {
        if (generations == 0 || person.children.isEmpty()) return

        val y0 = y + metrics.boxHeight / 2f
        val x1 = x
        var x2 = x - metrics.boxSpace / 2f

        addLine(x1, y0, x2, y0, lines)

        val dWeight = personDict.getValue(person).dWeight
        val vOffset = dWeight * (metrics.boxHeight + metrics.boxSpace) / 2f
        val v1Offset: Float
        val v2Offset: Float

        if (dWeight == 1) {
            v1Offset = vOffset
            v2Offset = vOffset
        } else {
            val firstChild = person.children.getElementAt(0)!!
            val lastChild = person.children.getElementAt(person.children.size - 1)!!
            v1Offset = (dWeight - personDict.getValue(firstChild).dWeight) * (metrics.boxHeight + metrics.boxSpace) / 2f
            v2Offset = (dWeight - personDict.getValue(lastChild).dWeight) * (metrics.boxHeight + metrics.boxSpace) / 2f
        }

        var y1 = y0 - v1Offset
        val yBottom = y0 + v2Offset

        addLine(x2, y1, x2, yBottom, lines)

        val xMid = x2 - metrics.boxSpace / 2f
        val x3 = xMid - metrics.boxWidth
        y1 = y1 + v1Offset - vOffset

        for (descendant in person.children) {
            val child = descendant!!
            y1 += personDict.getValue(child).dWeight * (metrics.boxHeight + metrics.boxSpace) / 2f
            addLine(xMid, y1, x2, y1, lines)

            val yBox = y1 - metrics.boxHeight / 2f
            addBox(child, x3, yBox, metrics, boxes)
            layoutDescendants(child, generations - 1, x3, yBox, metrics, personDict, boxes, lines)

            y1 += personDict.getValue(child).dWeight * (metrics.boxHeight + metrics.boxSpace) / 2f
        }
    }

    fun boxLabel(person: Person): String {
        return "${person.lastName.orEmpty()} ${person.firstName.orEmpty()}".trim()
    }
}

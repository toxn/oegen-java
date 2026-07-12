package fr.cdbs.oegen.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import fr.cdbs.oegen.data.Person

@Composable
fun PersonTreeCanvas(
    center: Person?,
    generations: Int,
    personsRevision: Int,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val textStyle = MaterialTheme.typography.bodySmall
    val boxStroke = MaterialTheme.colorScheme.outline
    val lineColor = MaterialTheme.colorScheme.onSurface
    val centerFill = MaterialTheme.colorScheme.primaryContainer
    val defaultFill = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    val metrics = remember(textMeasurer, textStyle) {
        val charWidth = textMeasurer.measure("M", style = textStyle).size.width.toFloat()
        val marriageSpace = charWidth + 2f
        val lineHeight = textMeasurer.measure("Mg", style = textStyle).size.height.toFloat()
        TreeMetrics(
            boxWidth = charWidth * 10f + 2f,
            boxHeight = lineHeight * 4f + 3f + 1f,
            boxSpace = marriageSpace / 2f,
            marriageSpace = marriageSpace,
        )
    }

    val layout = remember(center, generations, personsRevision, metrics) {
        center?.let { PersonTreeLayoutEngine.compute(it, generations, metrics) }
    }

    if (layout == null) {
        return
    }

    val horizontalScroll = rememberScrollState()
    val verticalScroll = rememberScrollState()

    Box(
        modifier = modifier
            .horizontalScroll(horizontalScroll)
            .verticalScroll(verticalScroll),
    ) {
        Canvas(
            modifier = Modifier
                .size(
                    width = with(density) { layout.width.toDp() },
                    height = with(density) { layout.height.toDp() },
                )
                .pointerInput(layout) {
                    detectTapGestures { offset ->
                        layout.boxes
                            .findLast { box ->
                                offset.x in box.left..box.left + box.width &&
                                    offset.y in box.top..box.top + box.height
                            }
                            ?.let { onPersonClick(it.person) }
                    }
                },
        ) {
            for (line in layout.lines) {
                drawLine(
                    color = lineColor,
                    start = Offset(line.x1, line.y1),
                    end = Offset(line.x2, line.y2),
                    strokeWidth = 1f,
                )
            }

            for (box in layout.boxes) {
                val isCenter = box.person === center
                drawRect(
                    color = if (isCenter) centerFill else defaultFill,
                    topLeft = Offset(box.left, box.top),
                    size = Size(box.width, box.height),
                )
                drawRect(
                    color = boxStroke,
                    topLeft = Offset(box.left, box.top),
                    size = Size(box.width, box.height),
                    style = Stroke(width = 2f),
                )

                val label = PersonTreeLayoutEngine.boxLabel(box.person)
                val textLayout = textMeasurer.measure(
                    text = label,
                    style = textStyle.copy(color = textColor),
                    maxLines = 2,
                    constraints = Constraints(maxWidth = (box.width - 2 * metrics.boxMargin).toInt()),
                )
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        box.left + metrics.boxMargin,
                        box.top + metrics.boxMargin,
                    ),
                )
            }
        }
    }
}

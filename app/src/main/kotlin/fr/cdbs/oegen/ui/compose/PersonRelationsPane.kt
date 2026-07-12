package fr.cdbs.oegen.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.Messages

private enum class RelationsViewMode {
    Text,
    Tree,
}

@Composable
fun PersonRelationsPane(
    person: Person?,
    personsRevision: Int,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier,
) {
    var viewMode by rememberSaveable { mutableStateOf(RelationsViewMode.Text) }
    var generations by rememberSaveable { mutableIntStateOf(4) }

    Column(modifier = modifier.fillMaxHeight()) {
        RelationsToolbar(
            viewMode = viewMode,
            onViewModeChange = { viewMode = it },
            generations = generations,
            onZoomIn = {
                if (generations > 1) {
                    generations--
                }
            },
            onZoomOut = { generations++ },
            showZoom = viewMode == RelationsViewMode.Tree,
        )

        when (viewMode) {
            RelationsViewMode.Text -> PersonRelationsText(
                person = person,
                onPersonClick = onPersonClick,
                modifier = Modifier.fillMaxSize(),
            )
            RelationsViewMode.Tree -> PersonTreeCanvas(
                center = person,
                generations = generations,
                personsRevision = personsRevision,
                onPersonClick = onPersonClick,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun RelationsToolbar(
    viewMode: RelationsViewMode,
    onViewModeChange: (RelationsViewMode) -> Unit,
    generations: Int,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    showZoom: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilterChip(
            selected = viewMode == RelationsViewMode.Text,
            onClick = { onViewModeChange(RelationsViewMode.Text) },
            label = { Text(Messages.getString("Action.Person.Relations.Show.Text.Name")) },
        )
        FilterChip(
            selected = viewMode == RelationsViewMode.Tree,
            onClick = { onViewModeChange(RelationsViewMode.Tree) },
            label = { Text(Messages.getString("Action.Person.Relations.Show.Tree.Name")) },
        )

        if (showZoom) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onZoomIn, enabled = generations > 1) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = Messages.getString("Action.Person.Relations.Zoom.In.Name"),
                    )
                }
                Text(
                    text = generations.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                IconButton(onClick = onZoomOut) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = Messages.getString("Action.Person.Relations.Zoom.Out.Name"),
                    )
                }
            }
        }
    }
}

@Composable
private fun PersonRelationsText(
    person: Person?,
    onPersonClick: (Person) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = Messages.getString("Person.Relations.Title"),
            style = MaterialTheme.typography.titleLarge,
        )

        if (person == null) {
            Text(
                text = Messages.getString("Person.Detail.NoneSelected"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            return
        }

        Text(
            text = formatPersonHeading(person),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
        )

        RelationSection(Messages.getString("Person.Relations.Parents")) {
            person.father?.let { RelationLine(it, onPersonClick) }
            person.mother?.let { RelationLine(it, onPersonClick) }
        }

        RelationSection(Messages.getString("Person.Relations.Siblings")) {
            person.siblings?.forEach { sibling ->
                RelationLine(sibling, onPersonClick)
            }
        }

        RelationSection(Messages.getString("Person.children")) {
            person.children.forEach { child ->
                RelationLine(child!!, onPersonClick)
            }
        }
    }
}

@Composable
private fun RelationSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        content()
    }
}

@Composable
private fun RelationLine(person: Person, onPersonClick: (Person) -> Unit) {
    Text(
        text = "• ${formatPersonHeading(person)}",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPersonClick(person) }
            .padding(start = 8.dp, bottom = 2.dp),
    )
}

private fun formatPersonHeading(person: Person): String {
    return "${person.gender?.symbol.orEmpty()} ${person.lastName.orEmpty()} ${person.firstName.orEmpty()}".trim()
}

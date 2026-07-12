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
import androidx.compose.material.icons.filled.Print
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
import fr.cdbs.oegen.data.PersonSummaryBuilder
import fr.cdbs.oegen.data.date.FuzzyDateDisplay
import fr.cdbs.oegen.data.date.LifeEventKind
import fr.cdbs.oegen.ui.Messages
import fr.cdbs.oegen.ui.print.PrintActions

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
    var treeGenerations by rememberSaveable { mutableIntStateOf(4) }
    var summaryGenerations by rememberSaveable { mutableIntStateOf(3) }

    Column(modifier = modifier.fillMaxHeight()) {
        RelationsToolbar(
            viewMode = viewMode,
            onViewModeChange = { viewMode = it },
            generations = if (viewMode == RelationsViewMode.Tree) treeGenerations else summaryGenerations,
            onZoomIn = {
                if (viewMode == RelationsViewMode.Tree) {
                    if (treeGenerations > 1) treeGenerations--
                } else if (summaryGenerations > 1) {
                    summaryGenerations--
                }
            },
            onZoomOut = {
                if (viewMode == RelationsViewMode.Tree) {
                    treeGenerations++
                } else {
                    summaryGenerations++
                }
            },
            onPrint = {
                when (viewMode) {
                    RelationsViewMode.Tree -> PrintActions.printTree(person, treeGenerations)
                    RelationsViewMode.Text -> PrintActions.printSummary(person, summaryGenerations)
                }
            },
            printEnabled = person != null,
        )

        when (viewMode) {
            RelationsViewMode.Text -> PersonRelationsText(
                person = person,
                summaryGenerations = summaryGenerations,
                onPersonClick = onPersonClick,
                modifier = Modifier.fillMaxSize(),
            )
            RelationsViewMode.Tree -> PersonTreeCanvas(
                center = person,
                generations = treeGenerations,
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
    onPrint: () -> Unit,
    printEnabled: Boolean,
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
            IconButton(onClick = onPrint, enabled = printEnabled) {
                Icon(
                    Icons.Default.Print,
                    contentDescription = Messages.getString("Action.Person.Relations.Print.Name"),
                )
            }
        }
    }
}

@Composable
private fun PersonRelationsText(
    person: Person?,
    summaryGenerations: Int,
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
            text = Messages.getString("Person.Summary.Title"),
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

        val lifeEvents = LifeEventKind.entries.mapNotNull { kind ->
            val date = person.lifeEvents.get(kind)
            val place = person.lifeEvents.getPlace(kind)
            if (date == null && place.isNullOrBlank()) return@mapNotNull null
            Triple(kind, date?.let { FuzzyDateDisplay.format(it) }, place)
        }
        if (lifeEvents.isNotEmpty()) {
            RelationSection(Messages.getString("Person.Summary.LifeEvents")) {
                lifeEvents.forEach { (kind, date, place) ->
                    val details = buildList {
                        date?.let { add(it) }
                        place?.takeIf { it.isNotBlank() }?.let { add(it) }
                    }.joinToString(" — ")
                    Text(
                        text = "• ${Messages.getString(kind.messageKey)}: $details",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
                    )
                }
            }
        }

        RelationSection(Messages.getString("Person.Relations.Parents")) {
            person.father?.let { RelationLine(it, onPersonClick) }
            person.mother?.let { RelationLine(it, onPersonClick) }
        }

        PersonSummaryBuilder.ancestorGenerations(person, summaryGenerations).forEachIndexed { index, generation ->
            RelationSection(Messages.getString("Person.Summary.AncestorsGeneration", index + 1)) {
                generation.forEach { ancestor ->
                    RelationLine(ancestor, onPersonClick)
                }
            }
        }

        RelationSection(Messages.getString("Person.Relations.Siblings")) {
            person.siblings?.forEach { sibling ->
                RelationLine(sibling, onPersonClick)
            }
        }

        val marriages = PersonSummaryBuilder.marriageGroups(person)
        if (marriages.isNotEmpty()) {
            RelationSection(Messages.getString("Person.Summary.Marriages")) {
                marriages.forEach { group ->
                    val spouseLabel = group.spouse?.let { formatPersonHeading(it) }
                        ?: Messages.getString("Person.Summary.UnknownSpouse")
                    Text(
                        text = "• $spouseLabel",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp),
                    )
                    group.children.forEach { child ->
                        RelationLine(child, onPersonClick, indent = 16.dp)
                    }
                }
            }
        } else {
            RelationSection(Messages.getString("Person.children")) {
                person.children.filterNotNull().forEach { child ->
                    RelationLine(child, onPersonClick)
                }
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
private fun RelationLine(
    person: Person,
    onPersonClick: (Person) -> Unit,
    indent: androidx.compose.ui.unit.Dp = 8.dp,
) {
    Text(
        text = "• ${formatPersonHeading(person)}",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPersonClick(person) }
            .padding(start = indent, bottom = 2.dp),
    )
}

private fun formatPersonHeading(person: Person): String {
    return "${person.gender?.symbol.orEmpty()} ${person.lastName.orEmpty()} ${person.firstName.orEmpty()}".trim()
}

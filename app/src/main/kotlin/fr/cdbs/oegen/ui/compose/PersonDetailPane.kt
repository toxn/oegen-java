package fr.cdbs.oegen.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.cdbs.oegen.data.AlternateName
import fr.cdbs.oegen.data.AlternateNameKind
import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.data.date.LifeEventKind
import fr.cdbs.oegen.ui.Messages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailPane(
    person: Person?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = Messages.getString("Person.Person"),
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

        OutlinedTextField(
            value = person.firstName.orEmpty(),
            onValueChange = { person.firstName = it },
            label = { Text(Messages.getString("Person.firstName")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        OutlinedTextField(
            value = person.lastName.orEmpty(),
            onValueChange = { person.lastName = it },
            label = { Text(Messages.getString("Person.lastName")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        GenderField(person = person)

        OutlinedTextField(
            value = person.id.orEmpty(),
            onValueChange = { },
            readOnly = true,
            label = { Text(Messages.getString("Oebject.Id")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        HorizontalDivider()

        Text(
            text = Messages.getString("Person.Summary.LifeEvents"),
            style = MaterialTheme.typography.titleMedium,
        )

        LifeEventKind.entries.forEach { kind ->
            LifeEventFields(person = person, kind = kind)
        }

        HorizontalDivider()

        AlternateNamesSection(person = person)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlternateNamesSection(person: Person) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = Messages.getString("Person.AlternateNames.Title"),
            style = MaterialTheme.typography.titleMedium,
        )
        IconButton(onClick = {
            person.alternateNames = person.alternateNames + AlternateName()
        }) {
            Icon(Icons.Default.Add, contentDescription = Messages.getString("Person.AlternateNames.Add"))
        }
    }

    if (person.alternateNames.isEmpty()) {
        Text(
            text = Messages.getString("Person.AlternateNames.Empty"),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        return
    }

    person.alternateNames.forEachIndexed { index, alternate ->
        AlternateNameEditor(
            alternate = alternate,
            onUpdate = { updated ->
                person.alternateNames = person.alternateNames.toMutableList().also { it[index] = updated }
            },
            onRemove = {
                person.alternateNames = person.alternateNames.filterIndexed { i, _ -> i != index }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlternateNameEditor(
    alternate: AlternateName,
    onUpdate: (AlternateName) -> Unit,
    onRemove: () -> Unit,
) {
    var kindExpanded by remember(alternate) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExposedDropdownMenuBox(
                expanded = kindExpanded,
                onExpandedChange = { kindExpanded = !kindExpanded },
                modifier = Modifier.weight(1f),
            ) {
                OutlinedTextField(
                    value = Messages.getString(alternate.kind.messageKey),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(Messages.getString("Person.AlternateNames.Kind")) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(kindExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                )
                ExposedDropdownMenu(
                    expanded = kindExpanded,
                    onDismissRequest = { kindExpanded = false },
                ) {
                    AlternateNameKind.entries.forEach { kind ->
                        DropdownMenuItem(
                            text = { Text(Messages.getString(kind.messageKey)) },
                            onClick = {
                                onUpdate(alternate.copy(kind = kind))
                                kindExpanded = false
                            },
                        )
                    }
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = Messages.getString("Person.AlternateNames.Remove"))
            }
        }
        OutlinedTextField(
            value = alternate.firstName.orEmpty(),
            onValueChange = { onUpdate(alternate.copy(firstName = it)) },
            label = { Text(Messages.getString("Person.firstName")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = alternate.lastName.orEmpty(),
            onValueChange = { onUpdate(alternate.copy(lastName = it)) },
            label = { Text(Messages.getString("Person.lastName")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

@Composable
private fun LifeEventFields(person: Person, kind: LifeEventKind) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = Messages.getString(kind.messageKey),
            style = MaterialTheme.typography.labelLarge,
        )
        OutlinedTextField(
            value = dateFor(person, kind).toEditableText(),
            onValueChange = { setDateFor(person, kind, parseEditableDate(it)) },
            label = { Text(Messages.getString("Person.LifeEvent.Date")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(Messages.getString("Person.LifeEvent.Date.Examples")) },
        )
        OutlinedTextField(
            value = placeFor(person, kind).orEmpty(),
            onValueChange = { setPlaceFor(person, kind, it) },
            label = { Text(Messages.getString("Person.LifeEvent.Place")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

private fun dateFor(person: Person, kind: LifeEventKind) = when (kind) {
    LifeEventKind.BIRTH -> person.birthDate
    LifeEventKind.BAPTISM -> person.baptismDate
    LifeEventKind.MARRIAGE -> person.marriageDate
    LifeEventKind.DEATH -> person.deathDate
    LifeEventKind.FUNERAL -> person.funeralDate
}

private fun setDateFor(person: Person, kind: LifeEventKind, value: fr.cdbs.oegen.data.date.FuzzyDate?) {
    when (kind) {
        LifeEventKind.BIRTH -> person.birthDate = value
        LifeEventKind.BAPTISM -> person.baptismDate = value
        LifeEventKind.MARRIAGE -> person.marriageDate = value
        LifeEventKind.DEATH -> person.deathDate = value
        LifeEventKind.FUNERAL -> person.funeralDate = value
    }
}

private fun placeFor(person: Person, kind: LifeEventKind) = when (kind) {
    LifeEventKind.BIRTH -> person.birthPlace
    LifeEventKind.BAPTISM -> person.baptismPlace
    LifeEventKind.MARRIAGE -> person.marriagePlace
    LifeEventKind.DEATH -> person.deathPlace
    LifeEventKind.FUNERAL -> person.funeralPlace
}

private fun setPlaceFor(person: Person, kind: LifeEventKind, value: String) {
    when (kind) {
        LifeEventKind.BIRTH -> person.birthPlace = value
        LifeEventKind.BAPTISM -> person.baptismPlace = value
        LifeEventKind.MARRIAGE -> person.marriagePlace = value
        LifeEventKind.DEATH -> person.deathPlace = value
        LifeEventKind.FUNERAL -> person.funeralPlace = value
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderField(person: Person) {
    var expanded by remember { mutableStateOf(false) }
    val current = person.gender ?: Person.Gender.Unknown

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = current.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(Messages.getString("Person.Gender")) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Person.Gender.entries.forEach { gender ->
                DropdownMenuItem(
                    text = { Text("${gender.symbol} ${gender.nlsName}") },
                    onClick = {
                        person.gender = gender
                        expanded = false
                    },
                )
            }
        }
    }
}

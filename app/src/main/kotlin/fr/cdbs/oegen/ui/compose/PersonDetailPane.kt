package fr.cdbs.oegen.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.cdbs.oegen.data.Person
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

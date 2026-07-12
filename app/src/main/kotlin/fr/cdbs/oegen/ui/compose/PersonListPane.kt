package fr.cdbs.oegen.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.cdbs.oegen.data.Person
import fr.cdbs.oegen.ui.Messages

@Composable
fun PersonListPane(
    state: OegenAppState,
    modifier: Modifier = Modifier,
) {
    val persons = state.filteredPersons()
    val selected = state.selectedPerson

    Column(modifier = modifier.fillMaxHeight()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { state.searchQuery = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text(Messages.getString("Person.Search.Prompt")) },
            )
            IconButton(onClick = { state.createPerson() }) {
                Icon(Icons.Default.Add, contentDescription = Messages.getString("Action.Person.Create.Name"))
            }
            IconButton(
                onClick = {
                    selected?.let { state.removePersons(listOf(it)) }
                },
                enabled = selected != null,
            ) {
                Icon(Icons.Default.Delete, contentDescription = Messages.getString("Action.Person.Remove.Name"))
            }
        }

        HorizontalDivider()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(persons, key = { it.id ?: it.hashCode() }) { person ->
                val isSelected = person === selected
                Surface(
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { state.selectPerson(person) },
                ) {
                    Text(
                        text = formatPersonLabel(person),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

private fun formatPersonLabel(person: Person): String {
    val gender = person.gender?.symbol.orEmpty()
    return "$gender ${person.lastName.orEmpty()} ${person.firstName.orEmpty()}".trim()
}

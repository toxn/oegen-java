package fr.cdbs.oegen.ui.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import fr.cdbs.oegen.ui.Messages

@Composable
fun FrameWindowScope.OegenMenuBar(
    state: OegenAppState,
    onRequestClose: () -> Unit,
) {
    MenuBar {
        Menu(Messages.getString("MainWindow.FileMenu")) {
            Item(Messages.getString("Action.New.Name"), onClick = { DatabaseActions.newDatabase(state) })
            Item(Messages.getString("Action.Open.Name"), onClick = { DatabaseActions.open(state) })
            Separator()
            Item(Messages.getString("Action.Save.Name"), onClick = { DatabaseActions.save(state) })
            Item(Messages.getString("Action.SaveAs.Name"), onClick = { DatabaseActions.saveAs(state) })
            Separator()
            Item(Messages.getString("Action.Quit.Name"), onClick = onRequestClose)
        }
    }
}

@Composable
fun OegenApp(state: OegenAppState = remember { OegenAppState() }) {
    MaterialTheme(colorScheme = lightColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                PersonListPane(
                    state = state,
                    modifier = Modifier.weight(0.30f),
                )
                VerticalDivider()
                PersonRelationsPane(
                    person = state.selectedPerson,
                    personsRevision = state.personsRevision,
                    onPersonClick = state::selectPerson,
                    modifier = Modifier.weight(0.40f),
                )
                VerticalDivider()
                PersonDetailPane(
                    person = state.selectedPerson,
                    modifier = Modifier.weight(0.30f),
                )
            }
        }
    }
}

package fr.cdbs.oegen.ctrl

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import fr.cdbs.oegen.data.db.DatabaseService
import fr.cdbs.oegen.ui.compose.DatabaseActions
import fr.cdbs.oegen.ui.compose.OegenApp
import fr.cdbs.oegen.ui.compose.OegenAppState
import fr.cdbs.oegen.ui.compose.OegenMenuBar
import fr.cdbs.oegen.ui.swing.MainWindowSwing

fun main(args: Array<String>) {
    if (args.contains("--swing")) {
        launchSwing()
    } else {
        launchCompose()
    }
}

private fun launchSwing() {
    DatabaseService.initialize()
    DatabaseService.loadIntoMemory()
    val mainWin = MainWindowSwing()
    mainWin.buildUI()
    mainWin.title = "\u0152gen"
    mainWin.setSize(800, 600)
    mainWin.isVisible = true
}

private fun launchCompose() {
    DatabaseService.initialize()
    DatabaseService.loadIntoMemory()

    application {
        val state = OegenAppState()
        state.markSaved()

        Window(
            onCloseRequest = {
                if (DatabaseActions.confirmExit(state)) {
                    DatabaseService.close()
                    exitApplication()
                }
            },
            title = state.windowTitle,
            state = rememberWindowState(width = 1200.dp, height = 800.dp),
        ) {
            OegenMenuBar(
                state = state,
                onRequestClose = {
                    if (DatabaseActions.confirmExit(state)) {
                        DatabaseService.close()
                        exitApplication()
                    }
                },
            )
            OegenApp(state = state)
        }
    }
}

package com.paulnogas.loganalyzer.view

import com.paulnogas.loganalyzer.app.RuntimeConfig
import com.paulnogas.loganalyzer.events.ConfigChangedEvent
import javafx.stage.FileChooser
import tornadofx.*

class MyWorkspace : Workspace() {
    init {
        header.items.remove(backButton)
        header.items.remove(forwardButton)
        header.items.remove(refreshButton)
        header.items.remove(saveButton)
        header.items.remove(createButton)
        header.items.remove(deleteButton)
        headingContainer.hide()
        button("load config") {
            action {
                val file = chooseFile(filters = arrayOf(FileChooser.ExtensionFilter("Highlight Config File", "*.json")))
                if (file.isNotEmpty()) {
                    runAsyncWithProgress {
                        RuntimeConfig.loadConfig(file.first())
                    } ui {
                        fire(ConfigChangedEvent)
                    }
                }
            }
        }
    }
}
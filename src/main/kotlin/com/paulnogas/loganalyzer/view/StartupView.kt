package com.paulnogas.loganalyzer.view

import com.paulnogas.loganalyzer.Constants.HIGHLIGHT_PATTERNS_PRIORITY_ORDER
import com.paulnogas.loganalyzer.HighlightPattern
import com.paulnogas.loganalyzer.app.Styles
import com.paulnogas.loganalyzer.controller.FileProcessor
import com.paulnogas.loganalyzer.controller.SearchTextHandler
import com.paulnogas.loganalyzer.logging.Util.getLogger
import com.paulnogas.loganalyzer.model.*
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import tornadofx.*

class StartupView : View("Log Analyzer") {

    private val controller: FileProcessor by inject()
    private val logListModel = LogListModel(LogsListHolder())
    private val filePathModel = FilePathModel(FilePathHolder())
    private val searchTextModel = SearchTextModel(SearchTextHolder())

    var filteredMatches = emptyList<LineAndHighlight>()
    private var checkedFilters = mutableSetOf<HighlightPattern>()

    private val searchTextHandler = SearchTextHandler(controller, logListModel)

    override val root = vbox(20, Pos.TOP_CENTER) {
        prefHeight = 600.0
        prefWidth = 600.0
        isCenterShape = true
        label(filePathModel.modelFilePath)
        makePatternCheckboxes(this)
        textfield(searchTextModel.modelSearchText) {
            promptText = "Type to filter"
            textProperty().addListener { _, _, updatedText ->
                searchTextHandler.onTextUpdated(updatedText)
            }
        }
        button("Open") {
            addClass(Styles.blueButton)
            id = "openButton"
            action {
                val file = chooseFile(filters = arrayOf(FileChooser.ExtensionFilter("Log files", "*.log", "*.txt")))
                if (file.isNotEmpty()) {
                    runAsyncWithProgress {
                        controller.process(file.first())
                        filteredMatches = controller.filterPatternsAndSearch(checkedFilters, searchTextModel.modelSearchText.get())
                    } ui {
                        getLogger().debug("rebound on UI thread")
                        filePathModel.rebind {
                            item = FilePathHolder(file.first().toString())
                        }
                        logListModel.rebind {
                            item = LogsListHolder(filteredMatches)
                        }
                    }
                }
            }
        }
        listview<LineAndHighlight>(logListModel.modelLogs) {
            vgrow = Priority.ALWAYS
            cellFormat {
                style {
                    text = it.text
                    backgroundColor += it.highlightPattern.color
                }
            }
        }
    }

    private fun makePatternCheckboxes(parent: EventTarget) {
        hbox(alignment = Pos.CENTER) {
            for (filter in HIGHLIGHT_PATTERNS_PRIORITY_ORDER) {
                checkbox(text = filter.displayString) {
                    action {
                        when {
                            isSelected -> checkedFilters.add(filter)
                            else -> checkedFilters.remove(filter)
                        }
                        runAsyncWithProgress {
                            filteredMatches = controller.filterPatternsAndSearch(checkedFilters, searchTextModel.modelSearchText.get())
                        } ui {
                            logListModel.rebind {
                                item = LogsListHolder(filteredMatches)
                            }
                        }
                    }
                }
                rectangle {
                    width = 10.0
                    height = 10.0
                    fill = filter.color
                }
            }
        }.attachTo(parent)
    }
}

package com.paulnogas.loganalyzer.view

import com.paulnogas.loganalyzer.HighlightPattern
import com.paulnogas.loganalyzer.app.RuntimeConfig
import com.paulnogas.loganalyzer.app.Styles
import com.paulnogas.loganalyzer.controller.FileProcessor
import com.paulnogas.loganalyzer.controller.SearchTextHandler
import com.paulnogas.loganalyzer.events.ConfigChangedEvent
import com.paulnogas.loganalyzer.model.*
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class StartupView : Fragment("Log Analyzer") {

    private val controller: FileProcessor by inject()
    private val logListModel = LogListModel(LogsListHolder(controller.rawList))
    private val filePathModel = FilePathModel(FilePathHolder())
    private val searchTextModel = SearchTextModel(SearchTextHolder())

    var filteredMatches = emptyList<LineAndHighlight>()
    private var checkedHighlightBoxes = mutableSetOf<HighlightPattern>()

    private val searchTextHandler = SearchTextHandler(controller, logListModel)

    init {
        subscribe<ConfigChangedEvent> {
            // we need to re-parse the entire file since the old highlights are no more
            runAsync {
                checkedHighlightBoxes.clear()
                val previouslyOpenedFile = filePathModel.modelFilePath.get()
                if (previouslyOpenedFile.isNotEmpty()) {
                    controller.process(File(previouslyOpenedFile))
                    filteredMatches = controller.filterPatternsAndSearch(checkedHighlightBoxes, searchTextModel.modelSearchText.get())
                }
            } ui {
                logListModel.rebind {
                    item = LogsListHolder(filteredMatches)
                }
            }
            root.replaceChildren(createView())
        }
    }

    override var root = createView()

    private fun createView(): Parent {
        return vbox(20, Pos.TOP_CENTER) {
            prefHeight = 600.0
            prefWidth = 600.0
            isCenterShape = true
            label(filePathModel.modelFilePath)
            buildHighlightCheckboxes(this)
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
                            filteredMatches = controller.filterPatternsAndSearch(checkedHighlightBoxes, searchTextModel.modelSearchText.get())
                        } ui {
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
                vgrow = Priority.SOMETIMES
                cellFormat {
                    style {
                        text = it.text
                        backgroundColor += it.highlightPattern.color
                    }
                }
            }
        }
    }

    private fun buildHighlightCheckboxes(parent: EventTarget) {
        flowpane {
            hgap = 5.0
            vgap = 5.0
            for (filter in RuntimeConfig.loadedHighlightPattern) {
                checkbox(text = filter.displayString) {
                    text {
                        background = filter.color.asBackground()
                    }
                    action {
                        when {
                            isSelected -> checkedHighlightBoxes.add(filter)
                            else -> checkedHighlightBoxes.remove(filter)
                        }
                        runAsyncWithProgress {
                            filteredMatches = controller.filterPatternsAndSearch(checkedHighlightBoxes, searchTextModel.modelSearchText.get())
                        } ui {
                            logListModel.rebind {
                                item = LogsListHolder(filteredMatches)
                            }
                        }
                    }
                }
            }
        }.attachTo(parent)
    }
}

package com.paulnogas.loganalyzer.view

import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import javafx.stage.FileChooser
import com.paulnogas.loganalyzer.Patterns
import com.paulnogas.loganalyzer.app.Styles
import com.paulnogas.loganalyzer.controller.FileProcessor
import com.paulnogas.loganalyzer.controller.SearchTextHandler
import com.paulnogas.loganalyzer.logging.Util.getLogger
import com.paulnogas.loganalyzer.model.*
import tornadofx.*

class StartupView : View("Log Analyzer") {

  private val controller: FileProcessor by inject()
  private val logListModel = LogListModel(LogsListHolder())
  private val filePathModel = FilePathModel(FilePathHolder())
  private val searchTextModel = SearchTextModel(SearchTextHolder())

  var allMatches = emptyList<LineAndPattern>()
  var filteredMatches = emptyList<LineAndPattern>()
  private var checkedPatterns = Patterns.values().toMutableSet()

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
            allMatches = controller.process(file.first())
            filteredMatches = controller.filterPatternsAndSearch(checkedPatterns, searchTextModel.modelSearchText.get() )
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
    listview<LineAndPattern>(logListModel.modelLogs) {
      vgrow = Priority.ALWAYS
      cellFormat {
        style {
          text = it.text
          backgroundColor += it.pattern.color
        }
      }
    }
  }

  private fun makePatternCheckboxes(parent: EventTarget) {
    hbox(alignment = Pos.CENTER) {
      for (pattern in Patterns.values()) {
        checkbox(text = pattern.displayString) {
          isSelected = true
          action {
            when {
              isSelected -> checkedPatterns.add(pattern)
              else -> checkedPatterns.remove(pattern)
            }
            runAsyncWithProgress {
              filteredMatches = controller.filterPatternsAndSearch(checkedPatterns, searchTextModel.modelSearchText.get())
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
          fill = pattern.color
        }
      }
    }.attachTo(parent)
  }
}

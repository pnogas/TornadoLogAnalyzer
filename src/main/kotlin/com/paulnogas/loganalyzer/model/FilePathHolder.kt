package com.paulnogas.loganalyzer.model

import javafx.beans.property.SimpleStringProperty

class FilePathHolder(filePath: String = "Choose a log file to analyze") {
  val filePathProperty = SimpleStringProperty(this, "filePath", filePath)
}

package com.paulnogas.loganalyzer.model

import javafx.beans.property.SimpleStringProperty

class FilePathHolder(filePath: String = "") {
    val filePathProperty = SimpleStringProperty(this, "filePath", filePath)
}

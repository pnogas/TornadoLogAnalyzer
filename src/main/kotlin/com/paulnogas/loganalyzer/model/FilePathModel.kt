package com.paulnogas.loganalyzer.model

import javafx.beans.property.ReadOnlyStringProperty
import tornadofx.*

class FilePathModel(fileHolder: FilePathHolder) : ItemViewModel<FilePathHolder>(fileHolder) {
  val modelFilePath = bind(FilePathHolder::filePathProperty) as ReadOnlyStringProperty
}

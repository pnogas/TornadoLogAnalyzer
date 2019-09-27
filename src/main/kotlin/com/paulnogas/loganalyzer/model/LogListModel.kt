package com.paulnogas.loganalyzer.model

import javafx.beans.property.ListProperty
import tornadofx.ItemViewModel

class LogListModel(logHolder: LogsListHolder) : ItemViewModel<LogsListHolder>(logHolder) {
    val modelLogs = bind(LogsListHolder::logListProperty) as ListProperty<LineAndHighlight>
}

package com.paulnogas.loganalyzer.model

import javafx.beans.property.SimpleListProperty
import tornadofx.*

class LogsListHolder(logList: List<LineAndHighlight>? = null) {
    val logListProperty = SimpleListProperty<LineAndHighlight>(this, "logList", logList?.asObservable())
}

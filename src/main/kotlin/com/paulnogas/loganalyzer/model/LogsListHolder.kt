package com.paulnogas.loganalyzer.model

import javafx.beans.property.SimpleListProperty
import tornadofx.*

class LogsListHolder(logList: List<LineAndPattern>? = null) {
  val logListProperty = SimpleListProperty<LineAndPattern>(this, "logList", logList?.observable())
  var logList by logListProperty
}

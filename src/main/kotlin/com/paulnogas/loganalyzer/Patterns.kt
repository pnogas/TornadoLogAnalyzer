package com.paulnogas.loganalyzer

import javafx.scene.paint.Color

enum class Patterns(val displayString: String, val regEx: Regex, val color: Color) {
  COMMON("Common errors", Regex("exception|crashed|death|fatal|killed| f | e ", RegexOption.IGNORE_CASE), Color.PINK),
}

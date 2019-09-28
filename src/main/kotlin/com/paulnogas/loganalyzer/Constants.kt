package com.paulnogas.loganalyzer

import javafx.scene.paint.Color

object Constants {
    val DEFAULT_HIGHLIGHT_PATTERNS_PRIORITY_ORDER = listOf(
            HighlightPattern("Common errors", Regex("exception|crashed|death|fatal|killed| f | e ", RegexOption.IGNORE_CASE), Color.PINK)
    )
    val HIGHLIGHT_PATTERN_NONE = HighlightPattern("", Regex(""), Color.WHITE)
}

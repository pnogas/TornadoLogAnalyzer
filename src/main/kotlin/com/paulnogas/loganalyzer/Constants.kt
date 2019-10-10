package com.paulnogas.loganalyzer

import javafx.scene.paint.Color

object Constants {
    const val COMMAND_LINE_PARAM_INITIAL_LOG_FILE = "logfile"
    const val COMMAND_LINE_INITIAL_CONFIG_FILE = "config"
    val DEFAULT_HIGHLIGHT_PATTERNS_PRIORITY_ORDER = listOf(
            HighlightPattern("Common errors", Regex("exception|crashed|death|fatal|killed| f | e ", RegexOption.IGNORE_CASE), Color.PINK)
    )
    val HIGHLIGHT_PATTERN_NONE = HighlightPattern("", Regex(""), Color.WHITE)
}

package com.paulnogas.loganalyzer.model

import com.paulnogas.loganalyzer.HighlightPattern

data class LineAndHighlight(val text: String, val highlightPattern: HighlightPattern)

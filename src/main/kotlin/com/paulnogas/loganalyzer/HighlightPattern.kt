package com.paulnogas.loganalyzer

import javafx.scene.paint.Color

data class HighlightPattern(val displayString: String, val regEx: Regex, val color: Color)

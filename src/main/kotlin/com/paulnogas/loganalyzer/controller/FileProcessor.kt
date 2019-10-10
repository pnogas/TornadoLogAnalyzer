package com.paulnogas.loganalyzer.controller

import com.paulnogas.loganalyzer.Constants.HIGHLIGHT_PATTERN_NONE
import com.paulnogas.loganalyzer.HighlightPattern
import com.paulnogas.loganalyzer.app.RuntimeConfig
import com.paulnogas.loganalyzer.model.LineAndHighlight
import tornadofx.Controller
import java.io.File

class FileProcessor : Controller() {
    internal var rawList: List<LineAndHighlight> = emptyList()
    internal var filteredList: List<LineAndHighlight> = emptyList()

    fun process(file: File): List<LineAndHighlight> {
        rawList = file.useLines { sequence ->
            sequence.mapNotNull { line ->
                parseLine(line)
            }.toList()
        }
        return rawList
    }

    private fun parseLine(line: String): LineAndHighlight? {
        for (filter in RuntimeConfig.loadedHighlightPattern) {
            if (filter.regEx.containsMatchIn(line)) {
                return LineAndHighlight(line, filter)
            }
        }
        return LineAndHighlight(line, HIGHLIGHT_PATTERN_NONE)
    }

    fun filterPatternsAndSearch(allowedPatternHighlightPatterns: Set<HighlightPattern>, searchString: String): List<LineAndHighlight> {
        filteredList = when {
            allowedPatternHighlightPatterns.isEmpty() -> rawList
            else -> rawList.filter { it.highlightPattern in allowedPatternHighlightPatterns }
        }
        return filterSearch(searchString)
    }

    fun filterSearch(searchString: String): List<LineAndHighlight> {
        searchString.ifBlank {
            return filteredList
        }
        return filteredList.filter {
            it.text.contains(searchString)
        }
    }
}

package com.paulnogas.loganalyzer.controller

import com.paulnogas.loganalyzer.Patterns
import com.paulnogas.loganalyzer.model.LineAndPattern
import tornadofx.*
import java.io.File

class FileProcessor : Controller() {
  private var rawList: List<LineAndPattern> = emptyList()
  private var patternFilteredList: List<LineAndPattern> = emptyList()

  fun process(file: File): List<LineAndPattern> {
    rawList = file.useLines { sequence ->
      sequence.mapNotNull { line ->
        parseLineOrNull(line)
      }.toList()
    }
    return rawList
  }

  private fun parseLineOrNull(line: String): LineAndPattern? {
    for (pattern in Patterns.values()) {
      if (pattern.regEx.containsMatchIn(line)) {
        return LineAndPattern(line, pattern)
      }
    }
    return null
  }

  fun filterPatternsAndSearch(allowedPatterns: Set<Patterns>, searchString: String): List<LineAndPattern> {
    patternFilteredList = rawList.filter { it.pattern in allowedPatterns }
    return filterSearch(searchString)
  }

  fun filterSearch(searchString: String): List<LineAndPattern> {
    searchString.ifBlank {
      return patternFilteredList
    }
    return patternFilteredList.filter {
      it.text.contains(searchString)
    }
  }
}

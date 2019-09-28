package com.paulnogas.loganalyzer.app

import com.paulnogas.loganalyzer.Constants
import com.paulnogas.loganalyzer.HighlightPattern
import com.paulnogas.loganalyzer.logging.Util.getLogger
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import javafx.scene.paint.Color
import okio.Okio
import java.io.File

object RuntimeConfig {
    // https://medium.com/@BladeCoder/advanced-json-parsing-techniques-using-moshi-and-kotlin-daf56a7b963d
    private val NAMES: JsonReader.Options = JsonReader.Options.of("name", "sound", "age")
    var loadedHighlightPattern = Constants.DEFAULT_HIGHLIGHT_PATTERNS_PRIORITY_ORDER

    fun loadConfig(file: File) {
        val result = mutableListOf<HighlightPattern>()
        Okio.buffer(Okio.source(file.inputStream())).use {
            val reader = JsonReader.of(it)
            parseJsonFile(reader, result)
        }
        getLogger().debug("Loaded config $result")
        loadedHighlightPattern = result
    }

    private fun parseJsonFile(platformReader: JsonReader?, result: MutableList<HighlightPattern>) {
        platformReader?.let { reader ->
            reader.beginObject()
            while (reader.hasNext()) {
                when (val propertyName = reader.nextName()) {
                    "highlights_priority_order" -> parseHighlightPattern(reader, result)
                    else -> {
                        getLogger().debug("Unexpected json property: $propertyName ignoring")
                        reader.skipValue()
                    }
                }
            }
            reader.endObject()
        } ?: getLogger().error("JsonReader was null due to library error!")
    }

    private fun parseHighlightPattern(platformReader: JsonReader?, result: MutableList<HighlightPattern>) {
        platformReader?.let { reader ->
            reader.beginArray()
            while (reader.hasNext()) {
                var name = ""
                var regex: Regex? = null
                var color: Color? = null
                reader.beginObject()
                while (reader.hasNext()) {
                    when (val propertyName = reader.nextName()) {
                        "name" -> name = reader.nextString()
                        "regex" -> regex = Regex(reader.nextString(), RegexOption.IGNORE_CASE)
                        "color" -> color = Color.valueOf(reader.nextString())
                        else -> {
                            getLogger().debug("Unexpected json property: $propertyName ignoring")
                            reader.skipValue()
                        }
                    }
                }
                reader.endObject()
                if (name.isEmpty() || regex == null || color == null) {
                    throw JsonDataException("Missing required field")
                }
                result.add(HighlightPattern(name, regex, color))
            }
            reader.endArray()
        } ?: getLogger().error("JsonReader was null due to library error!")
    }
}
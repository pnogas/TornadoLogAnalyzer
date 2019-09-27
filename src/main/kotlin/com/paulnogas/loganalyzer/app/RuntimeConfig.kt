package com.paulnogas.loganalyzer.app

import com.paulnogas.loganalyzer.logging.Util.getLogger
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import okio.Okio

object RuntimeConfig {
    // https://medium.com/@BladeCoder/advanced-json-parsing-techniques-using-moshi-and-kotlin-daf56a7b963d
    private val NAMES: JsonReader.Options = JsonReader.Options.of("name", "sound", "age")

    fun loadConfig() {
        val result = mutableListOf<Animal>()
        val url = javaClass.classLoader.getResource("a.json")
        url?.let {
            Okio.buffer(Okio.source(url.openStream())).use {
                val reader = JsonReader.of(it)
                parseJsonFile(reader, result)
            }
        } ?: getLogger().error("URL was null!")
        getLogger().debug(result.toString())
    }

    private fun parseJsonFile(platformReader: JsonReader?, result: MutableList<Animal>) {
        platformReader?.let { reader ->
            reader.beginObject()
            while (reader.hasNext()) {
                when (val propertyName = reader.nextName()) {
                    "animals" -> parseAnimalArray(reader, result)
                    else -> {
                        getLogger().debug("Unexpected property ${propertyName}")
                        reader.skipValue()
                    }
                }
            }
            reader.endObject()
        } ?: getLogger().error("JsonReader was somehow null!")
    }

    private fun parseAnimalArray(platformReader: JsonReader?, result: MutableList<Animal>) {
        platformReader?.let { reader ->
            reader.beginArray()
            while (reader.hasNext()) {
                var name = ""
                var sound = ""
                var age = -1
                reader.beginObject()
                while (reader.hasNext()) {
                    when (val propertyName = reader.nextName()) {
                        "name" -> name = reader.nextString()
                        "sound" -> sound = reader.nextString()
                        "age" -> age = reader.nextInt()
                        else -> {
                            getLogger().debug("Unexpected property ${propertyName}")
                            reader.skipValue()
                        }
                    }
                }
                reader.endObject()
                if (name.isEmpty() || sound.isEmpty() || age == -1) {
                    throw JsonDataException("Missing required field")
                }
                result.add(Animal(name, sound, age))
            }
            reader.endArray()
        } ?: getLogger().error("JsonReader was somehow null!")
    }
}

data class Animal(val name: String, val sound: String, val age: Int)
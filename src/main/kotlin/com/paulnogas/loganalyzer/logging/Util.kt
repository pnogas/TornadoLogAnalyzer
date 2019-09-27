package com.paulnogas.loganalyzer.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Util {
    fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)
}

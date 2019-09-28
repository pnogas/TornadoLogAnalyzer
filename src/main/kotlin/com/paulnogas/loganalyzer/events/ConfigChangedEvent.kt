package com.paulnogas.loganalyzer.events

import tornadofx.EventBus.RunOn.ApplicationThread
import tornadofx.FXEvent

object ConfigChangedEvent : FXEvent(ApplicationThread)

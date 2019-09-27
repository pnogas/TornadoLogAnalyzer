package com.paulnogas.loganalyzer.app

import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.cssclass

class Styles : Stylesheet() {
    companion object {
        val blueButton by cssclass()
    }

    init {
        blueButton {
            fontFamily = "Georgia"
            backgroundColor += Color.TEAL
            textFill = Color.WHITE
        }

    }
}

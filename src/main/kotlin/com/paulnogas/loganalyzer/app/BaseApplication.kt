package com.paulnogas.loganalyzer.app

import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import com.paulnogas.loganalyzer.view.StartupView
import tornadofx.*

class BaseApplication : App(StartupView::class, Styles::class) {

  override fun start(stage: Stage) {
    super.start(stage)
    setStageIcon(Image(resources.stream("/icon.png")))
    trayicon(resources.stream("/icon.png"), implicitExit = true) {
      setOnMouseClicked(fxThread = true) {
        FX.primaryStage.show()
        FX.primaryStage.toFront()
      }

      menu("MyApp") {
        item("Show...") {
          setOnAction(fxThread = true) {
            FX.primaryStage.show()
            FX.primaryStage.toFront()
          }
        }
        item("Exit") {
          setOnAction(fxThread = true) {
            Platform.exit()
          }
        }
      }
    }
  }
}

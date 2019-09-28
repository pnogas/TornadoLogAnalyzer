package com.paulnogas.loganalyzer.app

import com.paulnogas.loganalyzer.logging.Util.getLogger
import com.paulnogas.loganalyzer.view.MyWorkspace
import com.paulnogas.loganalyzer.view.StartupView
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App
import tornadofx.FX
import tornadofx.UIComponent
import tornadofx.setStageIcon

class BaseApplication : App(MyWorkspace::class, Styles::class) {

    override fun start(stage: Stage) {
        getLogger().debug("App started")
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

    override fun stop() {
        super.stop()
        getLogger().debug("App stopped")
    }

    override fun onBeforeShow(view: UIComponent) {
        with(workspace) {
            dock<StartupView>()
        }
    }
}

package com.paulnogas.loganalyzer.app

import com.paulnogas.loganalyzer.Constants.COMMAND_LINE_INITIAL_CONFIG_FILE
import com.paulnogas.loganalyzer.Constants.COMMAND_LINE_PARAM_INITIAL_LOG_FILE
import com.paulnogas.loganalyzer.controller.FileProcessor
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
import java.io.File
import java.nio.file.Paths

class BaseApplication : App(MyWorkspace::class, Styles::class) {
    private val controller: FileProcessor by inject()

    override fun start(stage: Stage) {
        getLogger().debug("App started")
        handleCommandLineParameters()
        super.start(stage)
        setStageIcon(Image(resources.stream("/icon.png")))
        /*trayicon(resources.stream("/icon.png"), implicitExit = true) {
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
        }*/
    }

    private fun handleCommandLineParameters() {
        if (parameters.named.containsKey(COMMAND_LINE_INITIAL_CONFIG_FILE)) {
            val initialConfigFileString = parameters.named[COMMAND_LINE_INITIAL_CONFIG_FILE]
            getLogger().debug("Try starting with configFile '$initialConfigFileString'")
            val initialConfigFile = getFirstExistingFile(initialConfigFileString.orEmpty())
            initialConfigFile?.let {
                RuntimeConfig.loadConfig(initialConfigFile)
            } ?: getLogger().debug("configFile does not exist")
        }
        if (parameters.named.containsKey(COMMAND_LINE_PARAM_INITIAL_LOG_FILE)) {
            val initialLogFileString = parameters.named[COMMAND_LINE_PARAM_INITIAL_LOG_FILE]
            getLogger().debug("Try starting with logFile '$initialLogFileString'")
            val initialLogFile = getFirstExistingFile(initialLogFileString.orEmpty())
            initialLogFile?.let {
                controller.process(initialLogFile)
            } ?: getLogger().debug("logFile does not exist")
        }
    }

    private fun getFirstExistingFile(filePath: String): File? {
        var result = File(filePath)
        if (result.exists() && result.isFile()) {
            return result
        }
        result = File(filePath.replaceFirst(Regex("^~"), System.getProperty("user.home")))
        if (result.exists() && result.isFile()) {
            return result
        }
        result = File(Paths.get("").toAbsolutePath().toString() + File.separator + filePath)
        if (result.exists() && result.isFile()) {
            return result
        }
        return null
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

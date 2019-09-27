package com.paulnogas.loganalyzer.app

import javafx.scene.control.Button
import javafx.scene.control.Labeled
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.stage.Stage
import com.paulnogas.loganalyzer.view.StartupView
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.util.DebugUtils.saveWindow
import org.testfx.util.NodeQueryUtils.hasText
import org.testfx.util.WaitForAsyncUtils.sleep
import tornadofx.*
import java.util.concurrent.TimeUnit
import java.util.function.Predicate

//@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension::class)
internal class ClickableButtonTest {
  /*@Mock
  private val fileDialogueFactory: FileDialogueFactory? = null

  @Mock
  private val fileDialogue: FileDialogue? = null*/

  /**
   * @param stage - Will be injected by the test runner.
   */
  @Start
  private fun start(stage: Stage) {
    val app = BaseApplication()
    val myScence = BaseApplication().createPrimaryScene(StartupView())
    FX.applyStylesheetsTo(myScence)
    stage.scene = myScence
    stage.show()
  }

  /**
   * @param robot - Will be injected by the test runner.
   */
  @Test
  fun simpleSelectorsDontThrow(robot: FxRobot) {
    val buttonByText = robot.lookup("Open").query<Button>()
    val buttonByNodeTextMatcher = robot.lookup(hasText("Open")).query<Button>()
    val buttonByCssId = robot.lookup(".blue-button").query<Button>()
    val buttonByCssClass = robot.lookup("#openButton").query<Button>()
    val buttonByCssSelectors = robot.lookup(".blue-button #openButton").query<Button>()
  }

  @Test
  fun simpleNodePropertyTest(robot: FxRobot) {
    verifyThat<Labeled>("#openButton", LabeledMatchers.hasText("Open"))
  }

  @Test
  fun complexNodePropertyTest(robot: FxRobot) {
    val buttonByText = robot.lookup("Open").query<Button>()
    val hasTealBackground = Predicate<Button> {
      it.background.fills[0].fill == Color.TEAL
    }
    //captureScreenshot() saves the WHOLE screen, saveWindow() is just our UI
    verifyThat(buttonByText, hasTealBackground, saveWindow())
  }

  @Test
  fun simpleInteractionTest(robot: FxRobot) {
    // when:
    robot.clickOn(".blue-button")
    sleep(1, TimeUnit.SECONDS)
    robot.press(KeyCode.ESCAPE)
  }
}

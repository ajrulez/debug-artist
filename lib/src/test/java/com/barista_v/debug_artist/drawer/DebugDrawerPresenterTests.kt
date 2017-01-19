package com.barista_v.debug_artist.drawer

import com.barista_v.debug_artist.MockFactory
import com.barista_v.debug_artist.drawer.item.LeakCanarySwitchMenuItem
import com.barista_v.debug_artist.drawer.item.LynksButtonMenuItem
import com.barista_v.debug_artist.drawer.item.PicassoLogsSwitchMenuItem
import com.barista_v.debug_artist.drawer.item.StethoSwitchMenuItem
import com.barista_v.debug_artist.drawer.item.input.InputItemListener
import com.barista_v.debug_artist.drawer.item.issue_reporter.ShakeDetector
import com.barista_v.debug_artist.mockSchedulers
import com.barista_v.debug_artist.repositories.BugRepository
import com.barista_v.debug_artist.utils.Device
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import kotlin.test.assertNull

//@RunWith(JUnitPlatform::class)
class DebugDrawerPresenterTests : Spek({
  mockSchedulers()

  describe("a new DebugDrawer presenter") {
    val view = mock<DebugDrawerView>()
    val actor = mock<Actor>()
    val shakeDetector = mock<ShakeDetector>()
    val traveler = mock<Traveler>()
    val device = mock<Device>()
    var presenter = DebugDrawerPresenter()

    beforeEachTest {
      Mockito.reset(view, traveler, actor, shakeDetector, device)

      presenter = DebugDrawerPresenter().apply {
        attach(view, traveler, actor, shakeDetector, device)
      }
    }

    on("pause") {
      presenter.deAttach()

      it("should release resources") {
        assertNull(presenter.view)
        assertNull(presenter.actor)
        assertNull(presenter.inputItemListener)
        assertNull(presenter.restartListener)
        verify(shakeDetector).pause()
      }
    }

    on("add switch stetho checked") {
      presenter.onItemAdded(StethoSwitchMenuItem(checked = true))

      it("should enable") {
        verify(actor).enableStetho()
        verify(view).addStethoSwitch(true)
      }
    }

    on("add switch stetho unchecked") {
      presenter.onItemAdded(StethoSwitchMenuItem())

      it("should not enable") {
        verifyZeroInteractions(actor)
        verify(view).addStethoSwitch(false)
      }
    }

    on("add switch Leak Canary checked") {
      presenter.onItemAdded(LeakCanarySwitchMenuItem(checked = true))

      it("should enable it") {
        verify(actor).enableLeakCanary()
        verify(view).addLeakCanarySwitch(true)
      }
    }

    on("add switch Leak Canary unchecked") {
      presenter.onItemAdded(LeakCanarySwitchMenuItem())

      it("should not enable") {
        verifyZeroInteractions(actor)
        verify(view).addLeakCanarySwitch(false)
      }
    }

    on("add switch Picasso Logs checked") {
      presenter.onItemAdded(PicassoLogsSwitchMenuItem(checked = true))

      it("should enable it") {
        verify(actor).enablePicassoLogs()
        verify(view).addPicassoLogsSwitch(true)
      }
    }

    on("add switch Picasso Logs unchecked") {
      presenter.onItemAdded(PicassoLogsSwitchMenuItem())

      it("should enable it") {
        verifyZeroInteractions(actor)
        verify(view).addPicassoLogsSwitch(false)
      }
    }

    on("add switch Scalpel Layout checked") {
      presenter.onItemAdded(MockFactory.scalpelSwitchMenuItem(checked = true))

      it("should enable it") {
        verify(actor).enableScalpelLayout()
        verify(view).addScalpelSwitch(true)
      }
    }

    on("add switch Scalpel Layout unchecked") {
      presenter.onItemAdded(MockFactory.scalpelSwitchMenuItem())

      it("should enable it") {
        verify(actor, times(0)).enableScalpelLayout()
        verify(view).addScalpelSwitch(false)
      }
    }

    on("add button Lynks") {
      presenter.onItemAdded(LynksButtonMenuItem())

      it("should add") {
        verify(view).addLynksButton()
      }
    }

    on("add button Phoenix") {
      presenter.onItemAdded(MockFactory.phoenixButtonMenuItem())

      it("should add") {
        verify(view).addPhoenixButton()
      }
    }

    on("add button Spinner") {
      val item = MockFactory.spinnerMenuItem()
      presenter.onItemAdded(item)

      it("should add") {
        verify(view).addSpinnerItem(item)
      }
    }

    on("add button Input") {
      val item = MockFactory.inputMenuItem()
      presenter.onItemAdded(item)

      it("should add") {
        verify(view).addInputItem(item)
      }
    }

    on("add button report bug") {
      val item = MockFactory.reportBugItem(true)
      presenter.onItemAdded(item)

      it("should add and start shake detector") {
        verify(view).addBugReportSwitch(true)
        verify(shakeDetector).start(presenter)
      }
    }

    on("lynks item selected") {
      presenter.onLynksItemSelected()

      it("should enable lynks") {
        verify(actor).enableLynx()
      }
    }

    on("phoenix item selected") {
      presenter.onPhoenixItemSelected()

      it("should trigger app rebirth") {
        verify(actor).triggerAppRebirth()
      }
    }

    on("picasso item selected") {
      presenter.onPicassoItemSelected()

      it("should enable picasso logs") {
        verify(actor).enablePicassoLogs()
      }
    }

    on("scalpel item checked") {
      presenter.onScalpelItemSelected(true)

      it("should enable scalpel") {
        verify(actor).enableScalpelLayout()
      }
    }

    on("scalpel item unchecked") {
      presenter.onScalpelItemSelected(false)

      it("should disable scalpel") {
        verify(actor).disableScalpelLayout()
      }
    }

    on("stetho item checked") {
      presenter.onStethoItemSelected()

      it("should enable stetho") {
        verify(actor).enableStetho()
      }
    }

    on("bug-reporter item checked") {
      presenter.onBugReporterItemSelected(true)

      it("should start listening for shakes") {
        verify(shakeDetector).start(presenter)
      }
    }

    on("bug-reporter item unchecked") {
      presenter.onBugReporterItemSelected(false)

      it("should pause listening for shakes") {
        verify(shakeDetector).pause()
      }
    }

    on("shake with count 2") {
      presenter.onShake(2)

      it("should do nothing") {
        verifyNoMoreInteractions(view, actor, traveler, shakeDetector, device)
      }
    }

    on("shake with right count and all files loaded") {
      val repositoryBuilder = mock<BugRepository.Builder>()

      whenever(device.takeScreenshot(anyString())).thenReturn("any.jpg")
      whenever(device.readLogFile()).thenReturn("log.log")

      presenter.bugRepositoryBuilder = repositoryBuilder
      presenter.onShake(1)

      it("should start report bug view") {
        verify(traveler).startBugReportView(repositoryBuilder, "any.jpg", "log.log")
      }
    }

    on("shake with right count and null screenshot file") {
      whenever(device.takeScreenshot(anyString())).thenReturn(null)
      whenever(device.readLogFile()).thenReturn("log.log")

      presenter.bugRepositoryBuilder = mock<BugRepository.Builder>()
      presenter.onShake(1)

      it("should do nothing") {
        verifyZeroInteractions(view, actor, shakeDetector, traveler)
      }
    }

    on("shake with right count and null log file") {
      whenever(device.takeScreenshot(anyString())).thenReturn("any.jpg")
      whenever(device.readLogFile()).thenReturn(null)

      presenter.bugRepositoryBuilder = mock<BugRepository.Builder>()
      presenter.onShake(1)

      it("should do nothing") {
        verifyZeroInteractions(view, actor, shakeDetector, traveler)
      }
    }

    on("text input entered") {
      val inputListener = mock<InputItemListener>()

      presenter.inputItemListener = inputListener
      presenter.onTextInputEntered(1, "asd")

      it("call listener with same values") {
        verify(inputListener).onTextInputEnter(1, "asd")
      }
    }

  }
})
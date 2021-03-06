package debug_artist.menu

import debug_artist.menu.drawer.item.*
import debug_artist.menu.report_bug.Answer
import org.mockito.Mockito.mock
import rx.Observable

object MockFactory {

  fun scalpelSwitchMenuItem(checked: Boolean = false): ScalpelSwitchMenuItem =
      ScalpelSwitchMenuItem(null, checked)

  fun phoenixButtonMenuItem(): PhoenixButtonMenuItem = mock(PhoenixButtonMenuItem::class.java)

  fun spinnerMenuItem(): SpinnerMenuItem = mock(SpinnerMenuItem::class.java)

  fun inputMenuItem(): InputMenuItem = mock(InputMenuItem::class.java)
  fun answer(): Observable<Answer<Any>>?
      = Observable.just(Answer())

  fun reportBugItem(checked: Boolean) = ReportBugSwitchMenuItem(checked, null)

}

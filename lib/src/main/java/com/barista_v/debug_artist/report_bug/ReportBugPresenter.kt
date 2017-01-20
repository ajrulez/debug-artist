package com.barista_v.debug_artist.report_bug

import com.barista_v.debug_artist.repositories.BugRepository
import com.barista_v.debug_artist.utils.extensions.composeForIoTasks

class ReportBugPresenter {

  private var view: ReportBugView? = null
  private var bugReportRepository: BugRepository? = null
  private var extrasHandler: ExtrasHandler? = null
  private var traveler: ReportBugTraveler? = null

  fun attach(view: ReportBugView, traveler: ReportBugTraveler, extrasHandler: ExtrasHandler) {
    this.view = view
    this.extrasHandler = extrasHandler
    this.bugReportRepository = extrasHandler.extraRepositoryBuilder.build()
    this.traveler = traveler

    view.setScreenshotImage(extrasHandler.screenshotFilePath)
  }

  fun onSendButtonClick(name: String, description: String) {
    if (name.isEmpty() || description.isEmpty()) {
      //TODO: Set error per field
      view?.showErrorDialog("All the fields are mandatory, please fill them.")
      return
    }

    view?.showProgressDialog()

    val screenshotFilePath = extrasHandler?.screenshotFilePath
    val logsFilePath = extrasHandler?.logsFilePath
    val createBugObservable = bugReportRepository?.create(name, description,
        screenshotFilePath, logsFilePath)

    createBugObservable?.composeForIoTasks()
        ?.doOnTerminate { view?.dismissProgressDialog() }
        ?.subscribe({
          if (it.error == null) {
            view?.showSuccessToast()
            traveler?.close()
          } else {
            view?.showErrorDialog(it.error.cause.toString())
          }
        }, {
          view?.showErrorDialog(it.message ?: "Something happened")
        })
  }

}
package com.paulnogas.loganalyzer.controller

import io.reactivex.subjects.PublishSubject
import com.paulnogas.loganalyzer.logging.Util.getLogger
import com.paulnogas.loganalyzer.model.LogListModel
import com.paulnogas.loganalyzer.model.LogsListHolder
import tornadofx.*
import java.util.concurrent.TimeUnit


class SearchTextHandler(private val controller: FileProcessor, private val logListModel: LogListModel) {
  private val searchSubject = PublishSubject.create<String>()

  init {
    searchSubject
      .debounce(500, TimeUnit.MILLISECONDS)
      .subscribe {
        it?.let{
          logListModel.rebind {
            getLogger().debug("rebound on Rx thread")
            item = LogsListHolder(controller.filterSearch(it))
          }
        } ?: getLogger().error("Search string was somehow null!")
      }
  }

  fun onTextUpdated(updatedText: String) {
    searchSubject.onNext(updatedText)
  }
}

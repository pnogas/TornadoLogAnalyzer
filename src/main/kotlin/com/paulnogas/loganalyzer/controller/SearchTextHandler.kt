package com.paulnogas.loganalyzer.controller

import com.paulnogas.loganalyzer.logging.Util.getLogger
import com.paulnogas.loganalyzer.model.LogListModel
import com.paulnogas.loganalyzer.model.LogsListHolder
import io.reactivex.subjects.PublishSubject
import tornadofx.rebind
import java.util.concurrent.TimeUnit


class SearchTextHandler(private val controller: FileProcessor, private val logListModel: LogListModel) {
    private val searchSubject = PublishSubject.create<String>()

    init {
        searchSubject
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    it?.let {
                        logListModel.rebind {
                            item = LogsListHolder(controller.filterSearch(it))
                        }
                    } ?: getLogger().error("Search string was null due to library error!")
                }
    }

    fun onTextUpdated(updatedText: String) {
        searchSubject.onNext(updatedText)
    }
}
